Reference : [Confluence - DCA Pipelines](https://confluence.equisoft.com/display/DATACONVERSION/DCA+Pipelines)

# AZURE ENVIRONMENTS
## Non-Prod Environments
Non production environments can be found in **DCA-NonProd** subscription in Azure

Resources are contained in 3 resource groups :
| Resource groups name | Description           |
| -------------------- | --------------------- |
| rg-glb-nonprod-cace  | Contains keyvault     |
| rg-data-nonprod-cace | Contains SQL Database |
| rg-apps-nonprod-cace | Contains Azure WebApp |

Shared resources across non-production environments are :
| Resource name           | Resource Group       | Description                                     |
| ----------------------- | -------------------- | ----------------------------------------------- |
| plan-dca-nprod-cace     | rg-apps-nonprod-cace | App Service Plan containing environment webapp  |
| sql-dca-nonprod-cace    | rg-data-nonprod-cace | SQL Server containing environment database      |
| kv-dca-nonprod-cace-001 | rg-glb-nonprod-cace  | Keyvault to store shared secret across Non-Prod |


### DEV
DEV Resources

|                           |                                                |
| ------------------------- | ---------------------------------------------- |
| **Application URL**       | https://app-dca-dev-cace-001.azurewebsites.net |
| **SQL Connection String** | jdbc:sqlserver://sql-dca-nprod-cace.database.windows.net:1433;database=sqldb-dcadb-dev-001;user=azureadmin@sql-dca-nprod-cace;password={your_password_here};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30; |

| Resource name         | Resource Group       | Description       |
| --------------------- | -------------------- | ----------------- |
| app-dca-dev-cace-001  | rg-apps-nonprod-cace | Azure App Service |
| sqldb-dcadb-dev-001   | rg-data-nonprod-cace | SQL Database      |
| kv-dca-dev-cace-001   | rg-glb-nonprod-cace  | Keyvault          |

### TST
TST Resources

|                           |                                                |
| ------------------------- | ---------------------------------------------- |
| **Application URL**       | https://app-dca-tst-cace-001.azurewebsites.net |
| **SQL Connection String** | jdbc:sqlserver://sql-dca-nprod-cace.database.windows.net:1433;database=sqldb-dcadb-tst-001;user=azureadmin@sql-dca-nprod-cace;password={your_password_here};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30; |

| Resource name         | Resource Group       | Description       |
| --------------------- | -------------------- | ----------------- |
| app-dca-tst-cace-001  | rg-apps-nonprod-cace | Azure App Service |
| sqldb-dcadb-tst-001   | rg-data-nonprod-cace | SQL Database      |
| kv-dca-tst-cace-001   | rg-glb-nonprod-cace  | Keyvault          |

# PIPELINES
Pipelines in this project use [Azure DevOps YAML Schema]. To be more re-usable, we also integrated [Azure DevOps YAML Templates] which allow identical task, job or stage to be re-used through different pipelines.

The pipelines folder use the following structure
```
pipelines                    ## Root folder for all pipelines
|--> scripts                 ## Script required by pipelines to prepare tasks
     |--> your-scripts.sh    
     |--> your-script.ps1

|--> templates               ## YAML Template which contains re-usable tasks, jobs or stages
     |--> your_template.yml

|--> variables               ## Variables files which contains non-sensitive data and are re-used through different pipeline
     |--> your_variables.yml

|--> your_pipelines.yml      ## Main pipeline file
```

## PIPELINES
### Code Validation - dca-code-validation.yml
The code validation pipeline run maven tasks to check style, bugs and execute unit tests. It uses the maven variables file (``variables/dca-mavenvariables.yml``) and the validation template (``dca-validation-stage.yml``).

***This pipelines is executed on every commit and when a pull request is created.***

If you want to add steps in **dca-validation-stage.yml** which run only for pull request or only for build you need to add a ``` condition: ``` in the task code. See example below :

``` yml
## To Add steps for Pull Request only, use a condition in the task block.
      - task: PublishBuildArtifacts@1
        condition: eq(variables['Build.Reason'], 'PullRequest')    # <--- this condition allow execution in pull request
        inputs:
          pathToPublish: '${{ parameters.artifactStagingDirectory }}'
          artifactName: drop
        displayName: Create Artifact

## To Add steps for build only, use a condition in the task block.
      - task: PublishBuildArtifacts@1
        condition: ne(variables['Build.Reason'], 'PullRequest')    # <--- this condition allow execution in build only
        inputs:
          pathToPublish: '${{ parameters.artifactStagingDirectory }}'
          artifactName: drop
        displayName: Create Artifact
```

### Build And Deploy - dca-app-builndeploy.yml
The build and deploy pipeline start by creating the DCA war file using the build stage (``dca-build-stage.yml``). Once complete DEV and TST environment are deployed using the deploy template (``dca-deploy-stage.yml``). While DEV environment is automatically updated, TST environment require approval to receive the new WAR file. It uses the maven variables files (``variables/dca-mavenvariables.yml``) to define maven parameters.

***This pipeline is executed when code is merged into develop and master branch.***

### SQL Data Import - dca-importsqlchema-cd.yml
The SQL Data Import pipeline is used to populate the SQL database. The build stage (``dca-sql-build-stage.yml``) create a package containing all sql scripts. Then the deploy stage (``dca-sql-deploy-stage.yml``) start by dropping all tables. Once complete, the complete structure is re-imported. While DEV environment is automatically updated, TST environment require approval to receive the update SQL schema.

Since SQL Data import require access to SQL Server and database, we use variables groups in this pipeline. The variable group is linked to the environment keyvault. It reference the secret inside the pipeline to allow authentication.

***This pipelines is manually executed.***

## ENVIRONMENTS
Environments can be found under Pipelines -> Environments.

Current naming is ``dca-nonprod-<environment>``

### DEV
There is no specific configuration for DEV environment

### TST
Test environment requires approval to be deployed.

### Update environment
1. In Azure DevOps, go to Pipelines -> Environments.
2. Click on the environment you want to update
3. Click on the 3 dots next to Add resource
4. Click on Approvals and checks
5. Configure the approvals and checks as required.

## VARIABLE GROUPS
Variable groups can be found in Pipelines -> Library

The variable groups are used reference keyvault secrets in pipeline. At creation time, each variable group is linked to a specific keyvault. Then secrets are selected and become available to pipeline using the variable group.

## Reference
 - [Azure DevOps Predefined Variables]
 - [Azure DevOps YAML Schema]
 - [Azure DevOps YAML Templates]

[Azure DevOps YAML Schema]: https://docs.microsoft.com/en-us/azure/devops/pipelines/yaml-schema?view=azure-devops&tabs=schema%2Cparameter-schema
[Azure DevOps YAML Templates]: (https://docs.microsoft.com/en-us/azure/devops/pipelines/process/templates?view=azure-devops#parameters)
[Azure DevOps Predefined Variables]: (https://docs.microsoft.com/en-us/azure/devops/pipelines/build/variables?view=azure-devops&tabs=yaml)