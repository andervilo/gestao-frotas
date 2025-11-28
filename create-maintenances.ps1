Write-Host "Consultando veiculos..." -ForegroundColor Cyan
$vehicles = Invoke-RestMethod -Uri "http://localhost:8080/api/vehicles" -Method Get
Write-Host "Total de veiculos: $($vehicles.Count)" -ForegroundColor Yellow

$maintenanceTypes = @('PREVENTIVE', 'CORRECTIVE', 'INSPECTION', 'TIRE_CHANGE', 'OIL_CHANGE')
$statuses = @('COMPLETED', 'SCHEDULED', 'IN_PROGRESS')

Write-Host "`nCriando manutencoes..." -ForegroundColor Cyan
$created = 0
$errors = 0

foreach ($vehicle in $vehicles) {
    # Criar 2-4 manutencoes por veiculo
    $numMaintenances = Get-Random -Minimum 2 -Maximum 5
    
    for ($i = 0; $i -lt $numMaintenances; $i++) {
        $type = $maintenanceTypes[(Get-Random -Minimum 0 -Maximum $maintenanceTypes.Count)]
        $status = $statuses[(Get-Random -Minimum 0 -Maximum $statuses.Count)]
        $daysAgo = Get-Random -Minimum 1 -Maximum 180
        $cost = Get-Random -Minimum 200 -Maximum 3000
        
        $body = @{
            vehicleId = $vehicle.id
            type = $type
            description = "Manutencao $type"
            cost = $cost
            status = $status
        }
        
        # Se for COMPLETED, adicionar todas as datas
        if ($status -eq 'COMPLETED') {
            $scheduledDate = (Get-Date).AddDays(-$daysAgo).ToString('yyyy-MM-ddTHH:mm:ss')
            $startDate = (Get-Date).AddDays(-$daysAgo + 1).ToString('yyyy-MM-ddTHH:mm:ss')
            $completionDate = (Get-Date).AddDays(-$daysAgo + (Get-Random -Minimum 2 -Maximum 4)).ToString('yyyy-MM-ddTHH:mm:ss')
            $body.scheduledDate = $scheduledDate
            $body.startDate = $startDate
            $body.completionDate = $completionDate
            $body.notes = "Manutencao concluida com sucesso"
        }
        # Se for SCHEDULED, adicionar apenas data agendada (futura)
        elseif ($status -eq 'SCHEDULED') {
            $scheduledDate = (Get-Date).AddDays((Get-Random -Minimum 5 -Maximum 60)).ToString('yyyy-MM-ddTHH:mm:ss')
            $body.scheduledDate = $scheduledDate
        }
        # Se for IN_PROGRESS, adicionar scheduled e start
        else {
            $scheduledDate = (Get-Date).AddDays(-$daysAgo).ToString('yyyy-MM-ddTHH:mm:ss')
            $startDate = (Get-Date).AddDays(-$daysAgo + 1).ToString('yyyy-MM-ddTHH:mm:ss')
            $body.scheduledDate = $scheduledDate
            $body.startDate = $startDate
            $body.notes = "Em andamento"
        }
        
        $bodyJson = $body | ConvertTo-Json
        
        try {
            Invoke-RestMethod -Uri "http://localhost:8080/api/maintenances" -Method Post -Body $bodyJson -ContentType "application/json" -ErrorAction Stop | Out-Null
            $created++
            if ($created % 25 -eq 0) { Write-Host "  Criadas: $created" -ForegroundColor Gray }
        } catch {
            $errors++
            if ($errors -le 3) {
                Write-Host "  Erro: $_" -ForegroundColor Red
            }
        }
    }
}

Write-Host "`n=== RESULTADO ===" -ForegroundColor Cyan
Write-Host "Manutencoes criadas: $created" -ForegroundColor Green
Write-Host "Erros: $errors" -ForegroundColor $(if($errors -gt 0){'Yellow'}else{'Green'})

# Exibir estatisticas
Start-Sleep -Seconds 2
$maintenances = Invoke-RestMethod -Uri "http://localhost:8080/api/maintenances" -Method Get

Write-Host "`n=== ESTATISTICAS ===" -ForegroundColor Cyan
Write-Host "Total: $($maintenances.Count)" -ForegroundColor Yellow

$byStatus = $maintenances | Group-Object status | Select-Object @{Name='Status';Expression={$_.Name}}, @{Name='Quantidade';Expression={$_.Count}}
$byType = $maintenances | Group-Object type | Select-Object @{Name='Tipo';Expression={$_.Name}}, @{Name='Quantidade';Expression={$_.Count}}

Write-Host "`nPor Status:" -ForegroundColor Yellow
$byStatus | Format-Table -AutoSize

Write-Host "Por Tipo:" -ForegroundColor Yellow
$byType | Format-Table -AutoSize
