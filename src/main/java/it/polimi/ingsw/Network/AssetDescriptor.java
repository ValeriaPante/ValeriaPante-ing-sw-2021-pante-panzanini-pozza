package it.polimi.ingsw.Network;

import java.io.ByteArrayInputStream;

public class AssetDescriptor {

    private final ByteArrayInputStream assetByteArrayI;
    private final String name;

    public AssetDescriptor(ByteArrayInputStream byteArrayInputStream, String assetName){
        this.assetByteArrayI = byteArrayInputStream;
        this.name = assetName;
    }

    public ByteArrayInputStream getAssetByteArrayI() {
        return this.assetByteArrayI;
    }
    public String getName() {
        return name;
    }
}
