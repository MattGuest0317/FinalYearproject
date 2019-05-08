package ModelDetections;


/**
 * An immutable result returned by a recognizer describing what was recognized.
 */
public final class Detection {
    /**
     * A unique identifier for what has been recognized. Specific to the class, not the instance of
     * the object.
     */
    private final Integer id;
    private final String title;
    private final Float confidence;
    private BoxPos pos;

    public Detection(final Integer id, final String title,
                       final Float confidence, final BoxPos pos) {
        this.id = id;
        this.title = title;
        this.confidence = confidence;
        this.pos = pos;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Float getConfidence() {
        return confidence;
    }

    public BoxPos getScaledLocation(final float scaleX, final float scaleY) {
        return new BoxPos(pos, scaleX, scaleY);
    }

    public BoxPos getLocation() {
        return new BoxPos(pos);
    }

    public void setLocation(BoxPos location) {
        this.pos = location;
    }

}
