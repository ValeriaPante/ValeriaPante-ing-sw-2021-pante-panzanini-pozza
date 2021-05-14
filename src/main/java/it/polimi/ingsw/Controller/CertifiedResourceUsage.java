package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Enums.Resource;

public abstract class CertifiedResourceUsage{
    protected boolean getLegalResource(Resource resource){
        if((resource == Resource.ANY) || (resource == Resource.WHITE) || (resource == Resource.FAITH))
            return false;

        return true;
    }
}
