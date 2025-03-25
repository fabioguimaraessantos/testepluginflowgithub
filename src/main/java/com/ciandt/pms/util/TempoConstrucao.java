package com.ciandt.pms.util;

/**
 * 
 * A classe TempoConstrucao proporciona a Thread de controle da progressBar de
 * construcao da pagina.
 * 
 * @since 13/04/2011
 * @author <a href="mailto:cmantovani@ciandt.com">Carlos Mantovani</a>
 * 
 */
public class TempoConstrucao extends Thread {

    // Controle da Thread de controle da progressBar de construcao da pagina

    /** Indicador se a Thread foi iniciada. */
    private boolean done = Boolean.FALSE;

    /** Indicador do tempo de consulta. */
    private int tempo = 0;

    /** Indicador do percentual de conclusao da construcao. */
    private float percentualConclusao = 0;

    /**
     * @return the percentualConclusao
     */
    public float getPercentualConclusao() {
        return percentualConclusao;
    }

    /**
     * @return the percentualConclusaoRounded
     */
    public float getPercentualConclusaoRounded() {
        return Math.round(percentualConclusao);
    }

    /**
     * @param percentualConclusao
     *            the percentualConclusao to set
     */
    public void setPercentualConclusao(final float percentualConclusao) {
        this.percentualConclusao = percentualConclusao;
    }

    /**
     * @return the done
     */
    public boolean isDone() {
        return done;
    }

    /**
     * @param done
     *            the done to set
     */
    public void setDone(final boolean done) {
        this.done = done;
    }

    /**
     * @return the tempo
     */
    public int getTempo() {
        return tempo;
    }

    /**
     * @param tempo
     *            the tempo to set
     */
    public void setTempo(final int tempo) {
        this.tempo = tempo;
    }

    /**
     * Método run - executa a thread.
     */
    public void run() {
        while (!isDone()) {
            for (int i = 0; i <= tempo; i++) {
                try {
                    Thread.sleep(1000);

                    float iteracao = i;

                    this.percentualConclusao = (iteracao / tempo) * 100;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}