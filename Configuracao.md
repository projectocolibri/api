# Introdução #

Informação detalhada sobre como configurar os projectos.

# Projecto de Exemplo #

  1. Existe um projecto de EXEMPLO incluido que deve ser usado como base para desenvolver qualquer produto sobre a API
  1. Este projecto inclui código relativo ás funcionalidades básicas da API, e está mínimamente comentado
  1. Para testar o projecto, consultar a secção seguinte

# Correr um projecto #

  1. **Abrir o "Package Explorer"**; Expandir a árvore do projecto; Abrir o ficheiro "product" (ex: colibri.rcp.example.product")
  1. **Seleccionar a TAB "Overview"**; Executar "Launch an Eclipse Application"
  1. Se existirem erros, consultar a secção seguinte

# Configurar um projecto #

  1. **Abrir o menu "Run"**; Selecionar a opção "Run configurations"
  1. **Expandir a secção "Eclipse Application"**; Apagar a entrada relativa ao "product" do projecto (ex: colibri.rcp.example.product")
  1. **Abrir o "Package Explorer"**; Expandir a árvore do projecto; Abrir o ficheiro "product" (ex: colibri.rcp.example.product")
  1. **Seleccionar a TAB "Dependencies"**; Premir o botão "Remove All"; Premir o Botão "Add"; Seleccionar o Plugin principal (ex: colibri.rcp.example"); Premir o botão "Add Required Plugins"; Gravar
  1. **Seleccionar a TAB "Overview"**; Executar "Launch an Eclipse Application"

# Exportar um projecto #

  1. **Abrir o "Package Explorer"**; Expandir a árvore do projecto; Abrir o ficheiro do "product" (ex: colibri.rcp.example.product")
  1. **Seleccionar a TAB "Overview"**; Executar "Eclipse Product Export Wizard"
  1. **Escolher a pasta "Root directory"** (pasta criada automáticamente com o conteúdo do export) e "Destination directory" (pasta-arquivo EXISTENTE onde irão residir todos os exports); Premir o botão "Next"

# NOTAS #

  1. Os projectos podem ser configurados para usarem o "Eclipse delta-pack target platform". Esta configuração permite exportar o projecto para diversas plataformas numa única operação. Para que tal seja possível, é necessário instalar e configurar o "Delta-pack" para o Eclipse. Mais informação brevemente.