
package com.sos.joc.model.jobChain;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.sos.joc.model.order.OrderV;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * jobChainNode (volatile part)
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "state",
    "job",
    "level",
    "jobChain",
    "numOfOrders",
    "orders"
})
public class JobChainNodeV {

    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name")
    private String name;
    /**
     * jobChainNode state
     * <p>
     * 
     * 
     */
    @JsonProperty("state")
    @JacksonXmlProperty(localName = "state")
    private JobChainNodeState state;
    @JsonProperty("job")
    @JacksonXmlProperty(localName = "job")
    private JobChainNodeJobV job;
    /**
     * Only relevant for job chain with splits and syncs. For example to imagine splits/sync in the job chain list view with different indents
     * 
     */
    @JsonProperty("level")
    @JsonPropertyDescription("Only relevant for job chain with splits and syncs. For example to imagine splits/sync in the job chain list view with different indents")
    @JacksonXmlProperty(localName = "level")
    private Integer level;
    /**
     * job chain object is included in nestedJobChains collection
     * 
     */
    @JsonProperty("jobChain")
    @JsonPropertyDescription("job chain object is included in nestedJobChains collection")
    @JacksonXmlProperty(localName = "jobChain")
    private JobChainNodeJobChainV jobChain;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("numOfOrders")
    @JacksonXmlProperty(localName = "numOfOrders")
    private Integer numOfOrders;
    @JsonProperty("orders")
    @JacksonXmlProperty(localName = "order")
    @JacksonXmlElementWrapper(useWrapping = true, localName = "orders")
    private List<OrderV> orders = new ArrayList<OrderV>();

    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    @JacksonXmlProperty(localName = "name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * jobChainNode state
     * <p>
     * 
     * 
     */
    @JsonProperty("state")
    @JacksonXmlProperty(localName = "state")
    public JobChainNodeState getState() {
        return state;
    }

    /**
     * jobChainNode state
     * <p>
     * 
     * 
     */
    @JsonProperty("state")
    @JacksonXmlProperty(localName = "state")
    public void setState(JobChainNodeState state) {
        this.state = state;
    }

    @JsonProperty("job")
    @JacksonXmlProperty(localName = "job")
    public JobChainNodeJobV getJob() {
        return job;
    }

    @JsonProperty("job")
    @JacksonXmlProperty(localName = "job")
    public void setJob(JobChainNodeJobV job) {
        this.job = job;
    }

    /**
     * Only relevant for job chain with splits and syncs. For example to imagine splits/sync in the job chain list view with different indents
     * 
     */
    @JsonProperty("level")
    @JacksonXmlProperty(localName = "level")
    public Integer getLevel() {
        return level;
    }

    /**
     * Only relevant for job chain with splits and syncs. For example to imagine splits/sync in the job chain list view with different indents
     * 
     */
    @JsonProperty("level")
    @JacksonXmlProperty(localName = "level")
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * job chain object is included in nestedJobChains collection
     * 
     */
    @JsonProperty("jobChain")
    @JacksonXmlProperty(localName = "jobChain")
    public JobChainNodeJobChainV getJobChain() {
        return jobChain;
    }

    /**
     * job chain object is included in nestedJobChains collection
     * 
     */
    @JsonProperty("jobChain")
    @JacksonXmlProperty(localName = "jobChain")
    public void setJobChain(JobChainNodeJobChainV jobChain) {
        this.jobChain = jobChain;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("numOfOrders")
    @JacksonXmlProperty(localName = "numOfOrders")
    public Integer getNumOfOrders() {
        return numOfOrders;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("numOfOrders")
    @JacksonXmlProperty(localName = "numOfOrders")
    public void setNumOfOrders(Integer numOfOrders) {
        this.numOfOrders = numOfOrders;
    }

    @JsonProperty("orders")
    @JacksonXmlProperty(localName = "order")
    public List<OrderV> getOrders() {
        return orders;
    }

    @JsonProperty("orders")
    @JacksonXmlProperty(localName = "order")
    public void setOrders(List<OrderV> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name).append("state", state).append("job", job).append("level", level).append("jobChain", jobChain).append("numOfOrders", numOfOrders).append("orders", orders).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(level).append(numOfOrders).append(name).append(jobChain).append(orders).append(state).append(job).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JobChainNodeV) == false) {
            return false;
        }
        JobChainNodeV rhs = ((JobChainNodeV) other);
        return new EqualsBuilder().append(level, rhs.level).append(numOfOrders, rhs.numOfOrders).append(name, rhs.name).append(jobChain, rhs.jobChain).append(orders, rhs.orders).append(state, rhs.state).append(job, rhs.job).isEquals();
    }

}
