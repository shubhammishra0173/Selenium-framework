package stepDeifnition;

import io.cucumber.java.en.Given;
import pageClass.amazon_commonPage;
import singletone.SharedVariables;
import stepDefinition_common.AbstractSteps;

public class commonStepDefinitionAmazon extends AbstractSteps {

private boolean amazonLogin=false;
private final SharedVariables sharedVariables = SharedVariables.getInstance();
public amazon_commonPage getCommonObjects(){
return this.getPageObjectManager().getAmazonCommonObjects();
}

    @Given("user login into the amazon site {} with {} ,{},{}")
    public void userLoginIntoTheAmazonSiteUrlWithIBDUserIDPassword() {

}
}
