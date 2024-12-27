<template>
    <div class="container">

      <header class="header">
        <div class="logo-container">
          <div class="logo">USACH</div>
          <h1 class="title">USACH</h1>
        </div>
      </header>
  
  
      <main class="main-content">
        <div class="button-container">
        
          <button @click="goToPage('/project-management')" class="button-square">
            <span class="button-number">1.</span> Gestión de la Integracion
          </button>
  
  
          <button @click="goToPage('/another-page')" class="button-square">
            <span class="button-number">2.</span> Gestión del Alcance
          </button>
  
          <button class="button-square">
            <span class="button-number">3.</span> Gestión del Cronograma
          </button>
          <button  class="button-square">
            <span class="button-number">4.</span> Gestión de los Costos
          </button>
          <button  class="button-square">
            <span class="button-number">5.</span> Gestión de la Calidad
          </button>
          <button  class="button-square">
            <span class="button-number">6.</span> Gestión de las Comunicaciones
          </button>
          <button  class="button-square">
            <span class="button-number">7.</span> Gestión de los Riesgos
          </button>
          <button  class="button-square">
            <span class="button-number">8.</span> Gestión de las Adquisiciones
          </button>
          <button  class="button-square">
            <span class="button-number">9.</span> Gestión de
          </button>
          <button  class="button-square">
            <span class="button-number">10.</span> Gestión de
          </button>
          <button  class="button-square">
            <span class="button-number">11.</span> Gestión de
          </button>
        </div>
      </main>
  
      <!-- Footer -->
      <footer class="footer">
        <p>&copy; 2024 USACH. Todos los derechos reservados.</p>
      </footer>
    </div>
  </template>
  
  <script setup>
  import { useRouter } from 'vue-router';
  
  const router = useRouter();
  
  function goToPage(path) {
    router.push(path);
  }
  </script>
  
  <style scoped>
 
  html, body {
    height: 100%;
    margin: 0;
    font-family: Arial, sans-serif;
  }
  
  .container {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
  }
  
  .header {
    background-color: white;
    padding: 20px;
    border-bottom: 1px solid #ddd; 
  }
  
  .logo-container {
    display: flex;
    align-items: center;
    gap: 10px;
  }
  
  .logo {
    width: 40px;
    height: 40px;
    background-color: #00B8B0;
    color: white;
    font-weight: bold;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 22px;
  }
  
  .title {
    color: #333;
    font-size: 30px;
    font-weight: bold;
  }
  
  .main-content {
    flex-grow: 1;
    display: flex;
    justify-content: center;
    align-items: center;
    text-align: center;
    padding: 40px;
    background-color: white;
  }
  
  .button-container {
    display: grid;
    grid-template-columns: repeat(3, 1fr); /* Tres columnas */
    gap: 20px; /* Espacio entre botones */
    width: 70%; /* Limita el ancho de la grilla */
  }
  
  .button-square {
    background-color: #d3d3d3; /* Gris claro */
    color: #333;
    border: none;
    padding: 40px; /* Aumenta el tamaño del botón */
    font-size: 20px; /* Aumenta el tamaño de la fuente */
    font-weight: bold;
    cursor: pointer;
    transition: background-color 0.3s ease;
    border-radius: 8px; /* Botón más cuadrado con bordes redondeados */
    text-align: center;
    width: 100%; /* Hace que los botones sean más cuadrados */
    position: relative;
    min-width: 200px; /* Establece un tamaño mínimo para los botones */
  }
  
  .button-square:hover {
    background-color: #a9a9a9; /* Gris más oscuro al hacer hover */
  }
  
  .button-number {
    position: absolute;
    top: 10px;
    left: 10px;
    font-size: 22px;
    font-weight: bold;
    color: #333;
  }
  
  .footer {
    background-color: #00B8B0;
    color: white;
    text-align: center;
    padding: 20px;
    font-size: 18px;
  }
  </style>
  