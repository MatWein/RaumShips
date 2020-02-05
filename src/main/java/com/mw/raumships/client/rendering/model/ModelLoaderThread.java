package com.mw.raumships.client.rendering.model;

public class ModelLoaderThread extends Thread {

    private String modelPath;
    private Model model;
    public boolean finished;

    public ModelLoaderThread(String modelPath) {
        model = null;
        finished = false;

        this.modelPath = modelPath;
    }

    public Model getModel() {
        if (finished)
            return model;
        else
            return null;
    }

    @Override
    public void run() {
        Loader loader = new Loader();

        model = loader.loadModel(modelPath);
        finished = true;
    }

}
