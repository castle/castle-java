package io.castle.client.model;

/**
 * Information about risk policy used for the action.
 * <p>
 * See the documentation for the semantics of each case.
 * It can be null.
 *
 * @see <a href="https://castle.io/docs/authentication">Adaptive authentication</a>
 */
public class RiskPolicyResult {

    /**
     * id of the risk policy
     */
    private String id;

    /**
     * id of the risk policy revision
     */
    private String revisionId;

    /**
     * name of the risk policy
     */
    private String name;

    /**
     * type of the risk policy
     */
    private String type;


    public String getId() {
        return id;
    }

    public String getRevisionId() {
        return revisionId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}

