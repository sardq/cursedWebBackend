<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8" />
  <title>Протокол обследования</title>
  <style>
  
  @font-face {
    font-family: 'DejaVuSans';
    src: url('file:fonts/DejaVuSans.ttf') format('truetype');
  }

  @font-face {
    font-family: 'Roboto-Bold';
    src: url('file:fonts/Roboto-Bold.ttf') format('truetype');
    font-weight: bold;
  }
  
    body {
      font-family: 'Roboto', sans-serif;
      margin: 50px;
      line-height: 1.5;
    }
    h1 {
      text-align: center;
      font-family: 'Roboto', sans-serif;
      font-size: 24px;
      margin-bottom: 30px;
    }
    .section {
      margin-bottom: 20px;
    }
    .section h2 {
      font-size: 18px;
      font-family: 'Roboto', sans-serif;
      margin-bottom: 10px;
    }
    .info-table, .params-table {
      width: 100%;
      border-collapse: collapse;
      margin-bottom: 20px;
    }
    .info-table td, .params-table td {
      padding: 8px;
      border: 1px solid #ddd;
      vertical-align: top;
    }
    .dejavu {
      font-family: 'DejaVuSans', sans-serif;
    }
    .signature {
      font-family: 'DejaVuSans', sans-serif;
      margin-top: 50px;
      text-align: right;
    }
  </style>
</head>
<body>

<h1>ПРОТОКОЛ ОБСЛЕДОВАНИЯ</h1>

<table class="info-table">
  <tr>
    <td><strong>Пациент:</strong></td>
    <td>${patientFullName}</td>
  </tr>
  <tr>
    <td><strong>Тип обследования:</strong></td>
    <td>${examinationTypeName}</td>
  </tr>
  <tr>
    <td><strong>Дата обследования:</strong></td>
    <td>${date}</td>
  </tr>
</table>

<#if description?? && (description?length > 0)>
  <div class="section">
    <h2>Описание</h2>
    <p>${description}</p>
  </div>
</#if>

<#if conclusion?has_content>
  <div class="section">
    <h2>Заключение</h2>
    <p>${conclusion}</p>
  </div>
</#if>

<div class="section">
  <h2>Параметры обследования</h2>
  <#if parameters?? && (parameters?size > 0)>
    <table class="params-table">
      <#list parameters as param>
        <tr>
          <td><strong>${param.name()}</strong></td>
          <td>${param.value()}</td>
        </tr>
      </#list>
    </table>
  <#else>
    <p>Нет данных о параметрах</p>
  </#if>
</div>


<div class="signature">
  <h5 class="roboto">Врач: ___________________</h5>
  <h5 class="dejavu">Дата: ${.now?string("dd.MM.yyyy")}</h5>
</div>

</body>
</html>
