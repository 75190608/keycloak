package org.keycloak.testsuite.console.page.clients;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jboss.arquillian.graphene.page.Page;
import org.keycloak.representations.idm.ClientRepresentation;
import static org.keycloak.testsuite.auth.page.login.OIDCLogin.OIDC;
import static org.keycloak.testsuite.console.page.clients.CreateClientForm.OidcAccessType.*;
import org.keycloak.testsuite.console.page.fragment.OnOffSwitch;
import org.keycloak.testsuite.page.Form;
import static org.keycloak.testsuite.page.Form.getInputValue;
import static org.keycloak.testsuite.util.WaitUtils.*;
import org.keycloak.testsuite.util.Timer;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

/**
 *
 * @author tkyjovsk
 */
public class CreateClientForm extends Form {

    @FindBy(id = "clientId")
    private WebElement clientIdInput;

    @FindBy(id = "protocol")
    private Select protocolSelect;
    
    @Page
    private SAMLClientSettingsForm samlForm;

    public SAMLClientSettingsForm samlForm() {
        return samlForm;
    }

    public void setValues(ClientRepresentation client) {
        waitUntilElement(clientIdInput).is().present();

        setClientId(client.getClientId());
        setProtocol(client.getProtocol());
    }

    public String getClientId() {
        return getInputValue(clientIdInput);
    }

    public void setClientId(String clientId) {
        setInputValue(clientIdInput, clientId);
    }

    public enum OidcAccessType {
        BEARER_ONLY("bearer-only"),
        PUBLIC("public"),
        CONFIDENTIAL("confidential");
        
        private final String name;

        private OidcAccessType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public String getProtocol() {
        waitUntilElement(protocolSelect.getFirstSelectedOption()).is().present();
        return protocolSelect.getFirstSelectedOption().getText();
    }

    public void setProtocol(String protocol) {
        Timer.time();
        protocolSelect.selectByVisibleText(protocol);
        Timer.time("clientSettings.setProtocol()");
    }

    public class SAMLClientSettingsForm extends Form {

        public static final String SAML_ASSERTION_SIGNATURE = "saml.assertion.signature";
        public static final String SAML_AUTHNSTATEMENT = "saml.authnstatement";
	public static final String SAML_CLIENT_SIGNATURE = "saml.client.signature";
	public static final String SAML_ENCRYPT = "saml.encrypt";
	public static final String SAML_FORCE_POST_BINDING = "saml.force.post.binding";
	public static final String SAML_MULTIVALUED_ROLES = "saml.multivalued.roles";
	public static final String SAML_SERVER_SIGNATURE = "saml.server.signature";
	public static final String SAML_SIGNATURE_ALGORITHM = "saml.signature.algorithm";
	public static final String SAML_ASSERTION_CONSUMER_URL_POST = "saml_assertion_consumer_url_post";
	public static final String SAML_ASSERTION_CONSUMER_URL_REDIRECT = "saml_assertion_consumer_url_redirect";
	public static final String SAML_FORCE_NAME_ID_FORMAT = "saml_force_name_id_format";
	public static final String SAML_NAME_ID_FORMAT = "saml_name_id_format";
	public static final String SAML_SIGNATURE_CANONICALIZATION_METHOD = "saml_signature_canonicalization_method";
	public static final String SAML_SINGLE_LOGOUT_SERVICE_URL_POST = "saml_single_logout_service_url_post";
	public static final String SAML_SINGLE_LOGOUT_SERVICE_URL_REDIRECT = "saml_single_logout_service_url_redirect";
        
        @FindBy(xpath = ".//div[@class='onoffswitch' and ./input[@id='samlAuthnStatement']]")
        private OnOffSwitch samlAuthnStatement;
        @FindBy(xpath = ".//div[@class='onoffswitch' and ./input[@id='samlServerSignature']]")
        private OnOffSwitch samlServerSignature;
        @FindBy(xpath = ".//div[@class='onoffswitch' and ./input[@id='samlAssertionSignature']]")
        private OnOffSwitch samlAssertionSignature;
        @FindBy(id = "signatureAlgorithm")
        private Select signatureAlgorithm;
        @FindBy(id = "canonicalization")
        private Select canonicalization;
        @FindBy(xpath = ".//div[@class='onoffswitch' and ./input[@id='samlEncrypt']]")
        private OnOffSwitch samlEncrypt;
        @FindBy(xpath = ".//div[@class='onoffswitch' and ./input[@id='samlClientSignature']]")
        private OnOffSwitch samlClientSignature;
        @FindBy(xpath = ".//div[@class='onoffswitch' and ./input[@id='samlForcePostBinding']]")
        private OnOffSwitch samlForcePostBinding;
        @FindBy(xpath = ".//div[@class='onoffswitch' and ./input[@id='frontchannelLogout']]")
        private OnOffSwitch frontchannelLogout;
        @FindBy(xpath = ".//div[@class='onoffswitch' and ./input[@id='samlForceNameIdFormat']]")
        private OnOffSwitch samlForceNameIdFormat;
        @FindBy(id = "samlNameIdFormat")
        private Select samlNameIdFormat;
        
        @FindBy(xpath = "//fieldset[contains(@data-ng-show, 'saml')]//i")
        private WebElement fineGrainCollapsor;
        
        @FindBy(id = "consumerServicePost")
        private WebElement consumerServicePostInput;
        @FindBy(id = "consumerServiceRedirect")
        private WebElement consumerServiceRedirectInput;
        @FindBy(id = "logoutPostBinding")
        private WebElement logoutPostBindingInput;
        @FindBy(id = "logoutRedirectBinding")
        private WebElement logoutRedirectBindingInput;
        
        public void setValues(ClientRepresentation client) {
            waitUntilElement(fineGrainCollapsor).is().visible();
            
            Map<String, String> attributes = client.getAttributes();
            samlAuthnStatement.setOn("true".equals(attributes.get(SAML_AUTHNSTATEMENT)));
            samlServerSignature.setOn("true".equals(attributes.get(SAML_SERVER_SIGNATURE)));
            samlAssertionSignature.setOn("true".equals(attributes.get(SAML_ASSERTION_SIGNATURE)));
            if (samlServerSignature.isOn() || samlAssertionSignature.isOn()) {
                signatureAlgorithm.selectByVisibleText(attributes.get(SAML_SIGNATURE_ALGORITHM));
                canonicalization.selectByValue("string:" + attributes.get(SAML_SIGNATURE_CANONICALIZATION_METHOD));
            }
            samlEncrypt.setOn("true".equals(attributes.get(SAML_ENCRYPT)));
            samlClientSignature.setOn("true".equals(attributes.get(SAML_CLIENT_SIGNATURE)));
            samlForcePostBinding.setOn("true".equals(attributes.get(SAML_FORCE_POST_BINDING)));
            frontchannelLogout.setOn(client.isFrontchannelLogout());
            samlForceNameIdFormat.setOn("true".equals(attributes.get(SAML_FORCE_NAME_ID_FORMAT)));
            samlNameIdFormat.selectByVisibleText(attributes.get(SAML_NAME_ID_FORMAT));
            
            fineGrainCollapsor.click();
            waitUntilElement(consumerServicePostInput).is().present();
            
            setInputValue(consumerServicePostInput, attributes.get(SAML_ASSERTION_CONSUMER_URL_POST));
            setInputValue(consumerServiceRedirectInput, attributes.get(SAML_ASSERTION_CONSUMER_URL_REDIRECT));
            setInputValue(logoutPostBindingInput, attributes.get(SAML_SINGLE_LOGOUT_SERVICE_URL_POST));
            setInputValue(logoutRedirectBindingInput, attributes.get(SAML_SINGLE_LOGOUT_SERVICE_URL_REDIRECT));
        }
    }

}
