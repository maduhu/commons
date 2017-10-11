
package com.sos.joc.model.jobChain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * job chains with delivery date (permanent part)
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "deliveryDate",
    "jobChains",
    "nestedJobChains"
})
public class JobChainsP {

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    @JsonProperty("deliveryDate")
    private Date deliveryDate;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobChains")
    private List<JobChainP> jobChains = new ArrayList<JobChainP>();
    @JsonProperty("nestedJobChains")
    private List<JobChainP> nestedJobChains = new ArrayList<JobChainP>();

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     * @return
     *     The deliveryDate
     */
    @JsonProperty("deliveryDate")
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * delivery date
     * <p>
     * Current date of the JOC server/REST service. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     * @param deliveryDate
     *     The deliveryDate
     */
    @JsonProperty("deliveryDate")
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The jobChains
     */
    @JsonProperty("jobChains")
    public List<JobChainP> getJobChains() {
        return jobChains;
    }

    /**
     * 
     * (Required)
     * 
     * @param jobChains
     *     The jobChains
     */
    @JsonProperty("jobChains")
    public void setJobChains(List<JobChainP> jobChains) {
        this.jobChains = jobChains;
    }

    /**
     * 
     * @return
     *     The nestedJobChains
     */
    @JsonProperty("nestedJobChains")
    public List<JobChainP> getNestedJobChains() {
        return nestedJobChains;
    }

    /**
     * 
     * @param nestedJobChains
     *     The nestedJobChains
     */
    @JsonProperty("nestedJobChains")
    public void setNestedJobChains(List<JobChainP> nestedJobChains) {
        this.nestedJobChains = nestedJobChains;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(deliveryDate).append(jobChains).append(nestedJobChains).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobChainsP) == false) {
            return false;
        }
        JobChainsP rhs = ((JobChainsP) other);
        return new EqualsBuilder().append(deliveryDate, rhs.deliveryDate).append(jobChains, rhs.jobChains).append(nestedJobChains, rhs.nestedJobChains).isEquals();
    }

}
