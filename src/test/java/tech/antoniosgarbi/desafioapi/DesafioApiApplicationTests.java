package tech.antoniosgarbi.desafioapi;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({
        "tech.antoniosgarbi.desafioapi.service",
        "tech.antoniosgarbi.desafioapi.mapper"
})
 class DesafioApiApplicationTests {

}
