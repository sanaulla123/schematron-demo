<?xml version="1.0" encoding="utf-8"?>
<iso:schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:iso="http://purl.oclc.org/dsdl/schematron" 
	queryBinding='xslt2' schemaVersion='ISO19757-3'>
	<iso:pattern id="check">
		<iso:rule context="/node/someone">
			<iso:assert test= "count(.[@status='captain']) = 1">
				You cannont have more than one &lt;someone&gt; with status attribute = captain
			</iso:assert>
			<iso:assert test= "boolean(@id)">
				id is required attribute in &lt;someone&gt;.
			</iso:assert>
		</iso:rule>
	</iso:pattern>
</iso:schema>