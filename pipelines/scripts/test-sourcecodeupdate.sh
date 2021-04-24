#!/bin/sh

## https://pumpingco.de/blog/run-an-azure-pipelines-job-only-if-source-code-has-changed/

##                           job name               step name     variable name
#dependsOn: CheckChanges # <- Important: Mark previous job as dependency 
#condition: eq(dependencies.CheckChanges.outputs['check_changes.SOURCE_CODE_CHANGED'], 'true')

#PATH_FILTER="src/"
PATH_FILTER=$1
VARIABLE_NAME=$2

CHANGED_FILES=$(git diff HEAD HEAD~ --name-only)
MATCH_COUNT=0

echo "Checking for file changes..."
for FILE in $CHANGED_FILES
do
    if [[ $FILE == *$PATH_FILTER* ]]; then
        echo "MATCH:  ${FILE} changed"
        MATCH_FOUND=true
        MATCH_COUNT=$(($MATCH_COUNT+1))
    else
        echo "IGNORE: ${FILE} changed"
    fi
done

echo "$MATCH_COUNT match(es) for filter '$PATH_FILTER' found."
if [[ $MATCH_COUNT -gt 0 ]]; then
    echo "##vso[task.setvariable variable=$VARIABLE_NAME;isOutput=true]true"
    #echo "##vso[task.setvariable variable=$VARIABLE_NAME]true"
else
    echo "##vso[task.setvariable variable=$VARIABLE_NAME;isOutput=true]false"
    #echo "##vso[task.setvariable variable=$VARIABLE_NAME]false"
fi