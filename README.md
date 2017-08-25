# Common
 Common is a base component of all the other product.
 
 ## Config
 * PropertiesContainer will load the epocharch.properties from classpath by default, You also can specify the path to set a System property -Depocharch_config="/xxx/xxx/epocharch.properties"
 
 * Use PropertiesContainer.loadProperties(String filePath,String namespace) method to load other config file into container.
 
 * If your system already have config properties, PropertiesContainer.setAndMergeProperties(String namespace, Properties properties) method will be help to unify the config entry. 
 
 ## Deploy 
 * DeployLevel.SITE
 * DeployLevel.IDC
 * DeployLevel.ZONE
 
 ## Util
