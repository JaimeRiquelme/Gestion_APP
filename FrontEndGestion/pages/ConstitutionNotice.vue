<template>
    <div class="page-container">
      <main class="main-content">
        <div class="notice-container">
          <h1 class="notice-title">Antes de continuar, necesitamos crear el acta de constitución del proyecto</h1>
          
          <div class="notice-content">
            <p>
              El Acta de Constitución del Proyecto es un documento fundamental que identifica el propósito 
              del proyecto, estableciendo de manera clara los objetivos, la descripción general del producto o 
              servicio que se entregará y los hitos más relevantes que marcarán el avance del proyecto.
            </p>
            <p>
              Además, sirve como un acuerdo formal entre los involucrados, autorizando oficialmente el inicio 
              del proyecto y definiendo las expectativas iniciales, las partes interesadas y las principales 
              restricciones y suposiciones que influirán en su desarrollo.
            </p>
            <p>
              Recuerda que el acta de constitución es un documento que funciona como base para el resto 
              del proyecto. Debido a esto, la información presentada es una versión resumida y preliminar de 
              áreas que se revisarán con más detalle a lo largo de la planeación del proyecto.
            </p>
          </div>
  
          <button 
            @click="$router.push('/constitutionform')" 
            class="create-button"
          >
            CREAR
          </button>
        </div>
      </main>
    </div>
  </template>
  
  <style scoped>
  .page-container {
    min-height: 100vh;
    background-color: #f5f5f5;
  }
  
  .main-content {
    padding:13rem;
  }
  
  .notice-container {
    max-width: 800px;
    margin: 2rem auto;
    padding: 2rem;
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
  
  .notice-title {
    font-size: 1.5rem;
    color: #000000;
    margin-bottom: 2rem;
  }
  
  .notice-content {
    color: #333;
    line-height: 1.6;
  }
  
  .notice-content p {
    margin-bottom: 1rem;
  }
  
  .create-button {
    display: inline-block;
    margin-top: 2rem;
    padding: 0.75rem 2rem;
    background-color: #00B8B0;
    color: white;
    border: none;
    border-radius: 4px;
    font-size: 1rem;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .create-button:hover {
    background-color: #009B94;
  }
  </style>