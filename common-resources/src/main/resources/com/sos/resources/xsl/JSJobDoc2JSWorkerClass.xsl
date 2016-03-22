<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:jobdoc="http://www.sos-berlin.com/schema/scheduler_job_documentation_v1.1"
	xmlns:xhtml="http://www.w3.org/1999/xhtml"
	exclude-result-prefixes="jobdoc">

	<xsl:output method="text" encoding="iso-8859-1" indent="no" />

<xsl:param name="Category" required="no" as="xs:string"/>
<xsl:param name="ClassNameExtension" required="yes" as="xs:string"/>
<xsl:param name="ExtendsClassName"  required="yes" as="xs:string"/>
<xsl:param name="WorkerClassName"  required="yes" as="xs:string"/>
<xsl:param name="XMLDocuFilename"  required="yes" as="xs:string"/>
<xsl:param name="XSLTFilename"  required="yes" as="xs:string"/>

<xsl:param name="timestamp" required="yes" as="xs:string"/> 
<xsl:param name="package_name" required="yes" as="xs:string"/> 
<xsl:param name="standalone" required="yes" as="xs:string"/> 
<xsl:param name="sourcetype" required="yes" as="xs:string"/> 
<xsl:param name="keywords" required="no" as="xs:string"/>
<xsl:param name="default_lang" required="yes" as="xs:string"/>


	<xsl:variable name="nl" select="'&#xa;'" />

	<xsl:template match="//jobdoc:description">
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="jobdoc:job">
	<!--  the name of the class is derived from the attribute name of the job-tac -->
		<xsl:variable name="class_name">
			<xsl:value-of select="concat(./@name, $ClassNameExtension)" />
		</xsl:variable>

		<xsl:variable name="class_title">
			<xsl:value-of select="./@title" />
		</xsl:variable>

package <xsl:value-of select="$package_name" />;

import <xsl:value-of select="$package_name" />.<xsl:value-of select="$WorkerClassName" />;
import <xsl:value-of select="$package_name" />.<xsl:value-of select="$WorkerClassName" />Options;
import org.apache.log4j.Logger;
import com.sos.JSHelper.Basics.JSJobUtilitiesClass;
import com.sos.localization.*;
import com.sos.scheduler.messages.JSMessages;
import com.sos.JSHelper.Basics.JSJobUtilities;


public class <xsl:value-of select="$class_name" /> extends JSJobUtilitiesClass &lt;<xsl:value-of select="$class_name" />Options&gt;{  
	private final String					conClassName						= "<xsl:value-of select="$class_name" />";  //$NON-NLS-1$
	private static Logger		logger			= Logger.getLogger(<xsl:value-of select="$class_name" />.class);

	protected <xsl:value-of select="$class_name"/>Options	objOptions			= null;
    private JSJobUtilities      objJSJobUtilities   = this;


	/**
	 * 
	 * \brief <xsl:value-of select="$class_name" />
	 *
	 * \details
	 *
	 */
	public <xsl:value-of select="$class_name" />() {
		super(new <xsl:value-of select="$class_name" />getOptions());
	}

	/**
	 * 
	 * \brief Options - returns the <xsl:value-of select="$class_name" />OptionClass
	 * 
	 * \details
	 * The <xsl:value-of select="$class_name" />OptionClass is used as a Container for all Options (Settings) which are
	 * needed.
	 *  
	 * \return <xsl:value-of select="$class_name" />Options
	 *
	 */
	public <xsl:value-of select="$class_name" />Options getOptions() {

		@SuppressWarnings("unused")  //$NON-NLS-1$
		final String conMethodName = conClassName + "::Options";  //$NON-NLS-1$

		if (objOptions == null) {
			objOptions = new <xsl:value-of select="$class_name" />getOptions();
		}
		return objOptions;
	}

	/**
	 * 
	 * \brief Options - set the <xsl:value-of select="$class_name" />OptionClass
	 * 
	 * \details
	 * The <xsl:value-of select="$class_name" />OptionClass is used as a Container for all Options (Settings) which are
	 * needed.
	 *  
	 * \return <xsl:value-of select="$class_name" />Options
	 *
	 */
	public <xsl:value-of select="$class_name" />Options getOptions(final <xsl:value-of select="$class_name" />Options pobjOptions) {

		@SuppressWarnings("unused")  //$NON-NLS-1$
		final String conMethodName = conClassName + "::Options";  //$NON-NLS-1$

		objOptions = pobjOptions;
		return objOptions;
	}

	/**
	 * 
	 * \brief Execute - Start the Execution of <xsl:value-of select="$class_name" />
	 * 
	 * \details
	 * 
	 * For more details see
	 * 
	 * \see JobSchedulerAdapterClass 
	 * \see <xsl:value-of select="$class_name" />Main
	 * 
	 * \return <xsl:value-of select="$class_name" />
	 *
	 * @return
	 */
	public <xsl:value-of select="$class_name" /> Execute() throws Exception {
		final String conMethodName = conClassName + "::Execute";  //$NON-NLS-1$

        logger.debug(String.format(JSMessages.JSJ_I_110.get(), conMethodName ));

		try { 
			Options().CheckMandatory();
			logger.debug(Options().toString());
		}
		catch (Exception e) {
			logger.error(e.getMessage(),e);
			logger.error(String.format(JSMessages.JSJ_F_107.get(), conMethodName ),e);
            throw e;			
		}
		finally {
            logger.debug(String.format(JSMessages.JSJ_I_111.get(), conMethodName ));
		}
		
		return this;
	}

	public void init() {
		@SuppressWarnings("unused")  //$NON-NLS-1$
		final String conMethodName = conClassName + "::init";  //$NON-NLS-1$
		doInitialize();
	}

	private void doInitialize() {
	} // doInitialize

  

    /**
     * 
     * \brief replaceSchedulerVars
     * 
     * \details
     * Dummy-Method to make sure, that there is always a valid Instance for the JSJobUtilities.
     * \return 
     *
     * @param isWindows
     * @param pstrString2Modify
     * @return
     */
    @Override
    public String replaceSchedulerVars(boolean isWindows, String pstrString2Modify) {
        logger.debug("replaceSchedulerVars as Dummy-call executed. No Instance of JobUtilites specified.");
        return pstrString2Modify;
    }

    /**
     * 
     * \brief setJSParam
     * 
     * \details
     * Dummy-Method to make shure, that there is always a valid Instance for the JSJobUtilities.
     * \return 
     *
     * @param pstrKey
     * @param pstrValue
     */
    @Override
    public void setJSParam(String pstrKey, String pstrValue) {

    }

    @Override
    public void setJSParam(String pstrKey, StringBuffer pstrValue) {

    }

    /**
     * 
     * \brief setJSJobUtilites
     * 
     * \details
     * The JobUtilities are a set of methods used by the SSH-Job or can be used be other, similar, job-
     * implementations.
     * 
     * \return void
     *
     * @param pobjJSJobUtilities
     */
    public void setJSJobUtilites(JSJobUtilities pobjJSJobUtilities) {

        if (pobjJSJobUtilities == null) {
            objJSJobUtilities = this;
        }
        else {
            objJSJobUtilities = pobjJSJobUtilities;
        }
        logger.debug("objJSJobUtilities = " + objJSJobUtilities.getClass().getName());
    }



}  // class <xsl:value-of select="$class_name" />

</xsl:template>

	<xsl:template match="text()">
		<!-- 	<xsl:value-of select="normalize-space(.)"/> -->
	</xsl:template>


</xsl:stylesheet>