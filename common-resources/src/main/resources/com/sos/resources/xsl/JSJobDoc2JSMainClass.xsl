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

import org.apache.log4j.Logger;
import com.sos.JSHelper.Basics.JSToolBox;


/**
 * \class 		<xsl:value-of select="$class_name" /> - Main-Class for "<xsl:value-of select="$class_title" />"
 *
 * \brief MainClass to launch <xsl:value-of select="$WorkerClassName" /> as an executable command-line program
 *
 * This Class <xsl:value-of select="$class_name" /> is the worker-class.
 *
<xsl:copy-of select="//jobdoc/description/documentation[language=$default_lang]" />
 *
 * see \see <xsl:value-of select="$XMLDocuFilename" /> for (more) details.
 *
 * \verbatim ;
 * mechanicaly created by <xsl:value-of select="$XSLTFilename" /> from http://www.sos-berlin.com at <xsl:value-of select="$timestamp" /> 
 * \endverbatim
 */
public class <xsl:value-of select="$class_name" /> extends JSJobUtilitesClass &lt;<xsl:value-of select="$WorkerClassName" />Options &gt;{
	// see http://stackoverflow.com/questions/8275499/how-to-call-getclass-from-a-static-method-in-java
	private static Class&lt;?&gt; currentClass = new Object() { }.getClass().getEnclosingClass();
	private static final String conClassName = currentClass.getSimpleName();
	private static final Logger logger = Logger.getLogger(currentClass.getEnclosingClass());
	@SuppressWarnings("unused")
	private static final String conSVNVersion = "$Id$";

	/**
	 * 
	 * \brief main
	 * 
	 * \details
	 *
	 * \return void
	 *
	 * @param pstrArgs
	 * @throws Exception
	 */
	public final static void main(String[] pstrArgs) {

		final String conMethodName = conClassName + "::Main"; 

		logger.info("<xsl:value-of select="$WorkerClassName" /> - Main"); 
		logger.info(conSVNVersion);

		try {
			<xsl:value-of select="$WorkerClassName" /> objM = new <xsl:value-of select="$WorkerClassName" />(<xsl:value-of select="$WorkerClassName" />Options);
			<xsl:value-of select="$WorkerClassName" />Options objO = objM.Options();

			objO.AllowEmptyParameterList.setFalse();
			objO.CheckNotProcessedOptions.setTrue();
						
			objO.CommandLineArgs(pstrArgs);
			
			if (objO.CheckNotProcessedOptions.isTrue()) {
				if (objO.CheckNotProcessedOptions() == true) {
				}
				else {
					throw new JobSchedulerException("Unsupported or wrong Options found.");
				}
			}
			objM.Execute();
		}
		
		catch (Exception e) {
			System.err.println(conMethodName + ": " + "Error occured ..." + e.getMessage()); 
			e.printStackTrace(System.err);
			int intExitCode = 99;
			logger.error(String.format(new JSMsg("JSJ-E-105").get(), conMethodName, intExitCode), e);		
			System.exit(intExitCode);
		}
		
		logger.info(String.format(new JSMsg("JSJ-I-106").get(), conMethodName));		
	}

}  // class <xsl:value-of select="$class_name" />

</xsl:template>

	<xsl:template match="text()">
		<!-- 	<xsl:value-of select="normalize-space(.)"/> -->
	</xsl:template>


</xsl:stylesheet>