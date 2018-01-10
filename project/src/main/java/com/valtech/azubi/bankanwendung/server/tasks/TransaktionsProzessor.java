package com.valtech.azubi.bankanwendung.server.tasks;


import com.valtech.azubi.bankanwendung.model.core.AuszahlenException;
import com.valtech.azubi.bankanwendung.model.core.HibernateUtil;
import com.valtech.azubi.bankanwendung.model.core.NegativerBetragException;
import com.valtech.azubi.bankanwendung.model.core.TransaktionsTyp;
import com.valtech.azubi.bankanwendung.model.entity.Konto;
import com.valtech.azubi.bankanwendung.model.entity.Transaktion;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class TransaktionsProzessor {
private int intervallsec=30;
private boolean running=false;

    public static void main(String[] args) {
        TransaktionsProzessor tProzessor = new TransaktionsProzessor();
        tProzessor.setIntervallsec(5);
        Session session = HibernateUtil.openSession();
        tProzessor.start();
        session.close();
    }

    public int getIntervallsec() {
        return intervallsec;
    }

    public void setIntervallsec(int intervallsec) {
        this.intervallsec = intervallsec;
    }

    public TransaktionsProzessor() {

    }

    public void start(){
        running=true;
        Thread t = new Thread(this::process);
        t.start();

    }

    public void stop(){

        running=false;
    }

    private void process(){

        Session session = HibernateUtil.openSession();


        while (running){
            List<Transaktion> transaktionList;
            transaktionList = session.createNamedQuery("getTransaktionen").getResultList();

            for (Transaktion t:transaktionList) {
                System.out.println(t);

                Transaction transaction = session.beginTransaction();
                Konto vonKonto =t.getKonto();
                Konto anKonto=t.getZielkonto();


                try {
                    vonKonto.subBetrag(t.getBetrag());
                    session.save(vonKonto.getProtokoll().addEintrag(vonKonto, anKonto, TransaktionsTyp.UEBERWEISUNG_AUSGANG, t.getVzweck(), t.getBetrag()));
                    anKonto.addBetrag(t.getBetrag());
                    session.save(anKonto.getProtokoll().addEintrag(vonKonto, anKonto, TransaktionsTyp.UEBERWEISUNG_EINGANG, t.getVzweck(), t.getBetrag()));
                    session.delete(t);

                } catch (NegativerBetragException e) {
                    transaction.rollback();
                    e.printStackTrace();
                } catch (AuszahlenException e) {
                    transaction.rollback();
                    e.printStackTrace();
               }

                transaction.commit();
            }

            try {
                Thread.sleep(intervallsec*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        session.close();


    }



}
