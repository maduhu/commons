
package com.sos.joc.model.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.sos.joc.model.common.ConfigurationMime;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * order conf filter
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "jobschedulerId",
    "jobChain",
    "orderId",
    "mime"
})
public class OrderConfigurationFilter {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    @JacksonXmlProperty(localName = "jobschedulerId")
    private String jobschedulerId;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("jobChain")
    @JsonPropertyDescription("absolute path based on live folder of a JobScheduler object.")
    @JacksonXmlProperty(localName = "jobChain")
    private String jobChain;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("orderId")
    @JacksonXmlProperty(localName = "orderId")
    private String orderId;
    /**
     * configuration mime filter
     * <p>
     * The configuration can have a HTML representation where the HTML gets a highlighting via CSS classes.
     * 
     */
    @JsonProperty("mime")
    @JsonPropertyDescription("The configuration can have a HTML representation where the HTML gets a highlighting via CSS classes.")
    @JacksonXmlProperty(localName = "mime")
    private ConfigurationMime mime = ConfigurationMime.fromValue("XML");

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    @JacksonXmlProperty(localName = "jobschedulerId")
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jobschedulerId")
    @JacksonXmlProperty(localName = "jobschedulerId")
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("jobChain")
    @JacksonXmlProperty(localName = "jobChain")
    public String getJobChain() {
        return jobChain;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("jobChain")
    @JacksonXmlProperty(localName = "jobChain")
    public void setJobChain(String jobChain) {
        this.jobChain = jobChain;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("orderId")
    @JacksonXmlProperty(localName = "orderId")
    public String getOrderId() {
        return orderId;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("orderId")
    @JacksonXmlProperty(localName = "orderId")
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * configuration mime filter
     * <p>
     * The configuration can have a HTML representation where the HTML gets a highlighting via CSS classes.
     * 
     */
    @JsonProperty("mime")
    @JacksonXmlProperty(localName = "mime")
    public ConfigurationMime getMime() {
        return mime;
    }

    /**
     * configuration mime filter
     * <p>
     * The configuration can have a HTML representation where the HTML gets a highlighting via CSS classes.
     * 
     */
    @JsonProperty("mime")
    @JacksonXmlProperty(localName = "mime")
    public void setMime(ConfigurationMime mime) {
        this.mime = mime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("jobschedulerId", jobschedulerId).append("jobChain", jobChain).append("orderId", orderId).append("mime", mime).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobChain).append(jobschedulerId).append(orderId).append(mime).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OrderConfigurationFilter) == false) {
            return false;
        }
        OrderConfigurationFilter rhs = ((OrderConfigurationFilter) other);
        return new EqualsBuilder().append(jobChain, rhs.jobChain).append(jobschedulerId, rhs.jobschedulerId).append(orderId, rhs.orderId).append(mime, rhs.mime).isEquals();
    }

}
