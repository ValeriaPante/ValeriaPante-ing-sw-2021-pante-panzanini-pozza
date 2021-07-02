package it.polimi.ingsw.Network;

import java.io.ByteArrayInputStream;

/**
 * Representation a file with his name and his bytes
 */
public class AssetDescriptor {

    private final ByteArrayInputStream assetByteArrayI;
    private final String name;

    /**
     * Constructor
     * @param byteArrayInputStream associated with anything
     * @param assetName name associated with the byte array input stream
     */
    public AssetDescriptor(ByteArrayInputStream byteArrayInputStream, String assetName){
        this.assetByteArrayI = byteArrayInputStream;
        this.name = assetName;
    }

    /**
     * Getter
     * @return byte array
     */
    public ByteArrayInputStream getAssetByteArrayI() {
        return this.assetByteArrayI;
    }

    /**
     * Getter
     * @return the name
     */
    public String getName() {
        return name;
    }
}
