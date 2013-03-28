package org.andrewwinter.sip.sdp;

/**
 *
 * @author andrewwinter77
 */
public class Bandwidth {

    private final BandwidthModifier modifier;
    
    private final long bandwidth;
    
    /**
     * 
     * @param modifier
     * @param bandwidth
     */
    Bandwidth(
            final BandwidthModifier modifier,
            final long bandwidth) {
        
        this.modifier = modifier;
        this.bandwidth = bandwidth;
    }
            
    /**
     *
     * @return
     */
    public BandwidthModifier getModifier() {
        return modifier;
    }

    /**
     *
     * @return
     */
    public long getBandwidth() {
        return bandwidth;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return modifier + ":" + bandwidth;
    }
}
