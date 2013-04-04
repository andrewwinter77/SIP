package org.andrewwinter.sip.sdp;

/**
 *
 * @author andrewwinter77
 */
public class BandwidthBuilder {

    private BandwidthModifier modifier;
    
    private long bandwidth;
    
    /**
     * Set some legal defaults.
     */
    public BandwidthBuilder() {
    }

    private static void nullCheck(final Object obj, final String desc) {
        if (obj == null) {
            throw new IllegalArgumentException(desc + " must not be null.");
        }
    }

    /**
     *
     * @param modifier
     * @return
     */
    public BandwidthBuilder modifier(final BandwidthModifier modifier) {
        nullCheck(modifier, "modifier");
        this.modifier = modifier;
        return this;
    }
    
    /**
     *
     * @param bandwidth
     * @return
     */
    public BandwidthBuilder bandwidth(final long bandwidth) {
        this.bandwidth = bandwidth;
        return this;
    }

    /**
     *
     * @return
     */
    public Bandwidth build() {
        nullCheck(modifier, "modifier");
        return new Bandwidth(modifier, bandwidth);
    }
    
    /**
     *
     * @param str
     * @return
     */
    public static Bandwidth parse(final String str) {
        final String[] parts = str.split(":");
        
        BandwidthBuilder builder = new BandwidthBuilder();
        try {
            final BandwidthModifier modifier = BandwidthModifier.valueOf(parts[0]);
            builder = builder.modifier(modifier);
            builder = builder.bandwidth(Long.parseLong(parts[1]));
            return builder.build();
        } catch (IllegalArgumentException e) {
            // MUST ignore bandwidth fields with unknown modifiers.
            return null;
        }
    }
}
