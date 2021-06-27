package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Enums.Resource;

/**
 * This class provide a method for checking if a resource can be added to a container
 */
public abstract class CertifiedResourceUsage{
    /**
     * Checks if the resource passed as a parameter can be added to a storage
     * @param resource resource to be inspected
     * @return true if the resource can be added to a storage, false otherwise
     */
    protected boolean getLegalResource(Resource resource){
        if((resource == Resource.ANY) || (resource == Resource.WHITE) || (resource == Resource.FAITH))
            return false;

        return true;
    }
}
