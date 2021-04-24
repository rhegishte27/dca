param(
    [string] $sqlDatabaseName,
    [string] $sqlScriptsPath
)

ForEach ($file in (get-childitem -Path $sqlScriptsPath -Filter *.sql).FullName) {
    Write-Host $file
    ((Get-Content $file -Raw) -replace 'DcaDb', "$sqlDatabaseName") | Set-Content $file
}
