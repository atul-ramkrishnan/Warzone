package org.warzone.operations;

import org.warzone.entities.Country;
import org.warzone.entities.GameMap;

import java.io.IOException;
import java.util.Map;

/**
 * The ConquestMapIOAdapter class is an adapter class for maps of the "Conquest" format.
 */
public class ConquestMapIOAdapter extends GameMapIO{
    private ConquestMapIO d_conquestIO;

    /**
     * Constructor which sets Io
     * @param p_conquestIO Constructor of this class
     */
    public ConquestMapIOAdapter(ConquestMapIO p_conquestIO) {
        this.d_conquestIO = p_conquestIO;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public GameMap loadGameMapToEdit(StringBuilder p_fileContents) {
        return d_conquestIO.loadGameMapToEdit(p_fileContents);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public GameMap loadGameMap(StringBuilder p_fileContents) throws IOException {
        return d_conquestIO.loadGameMap(p_fileContents);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Map<String, Country> loadMap(String p_fileName) throws IOException {
        return d_conquestIO.loadMap(p_fileName);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void saveGameMap(String p_fileName) throws IOException {
        d_conquestIO.saveGameMap(p_fileName);
    }

}
