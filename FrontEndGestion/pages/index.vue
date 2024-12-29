<template>
  <div class="container">
    <!-- Barra de Navegación -->
    <header class="header">
      <div class="logo-container">
        <div class="logo">USACH</div>
        <h1 class="title">USACH</h1>
      </div>
    </header>

    <!-- Contenido Principal -->
    <main class="main-content">
      <!-- Contenedor de texto e imagen -->
      <div class="content-container">
        <div class="content">
          <!-- Texto en negrita y debajo texto en tamaño más pequeño -->
          <h2 class="subtitle">
            <span class="bold-text">
              Aprende a hacer los documentos para la gestión de proyectos
            </span>
          </h2>
          <p class="description">
            Aprende gestión de proyectos creando los documentos para las distintas áreas de gestión, para todos los proyectos que quieras
          </p>
          <button 
            @click="handleIngresar" 
            class="ingresar-button"
          >
            Ingresar
          </button>
        </div>

        <!-- Imagen de la lista de documentos a la derecha -->
        <div class="image-container">
          <img src="@/assets/documents-image.jpg" alt="Lista de documentos" class="document-image" />
        </div>
      </div>
    </main>

    <!-- Footer -->
    <footer class="footer">
      <p>&copy; 2024 USACH. Todos los derechos reservados.</p>
    </footer>
  </div>
</template>

<script setup>
// Importamos el composable useRouter para la redirección
import { useRouter } from 'vue-router'

// Inicializamos el router
const router = useRouter()

// Función para manejar el clic en el botón y redirigir al login
const handleIngresar = () => {
  // Redirigir al usuario a la página de login
  router.push({ name: 'login' }) // Redirige a la página de login
}
</script>

<style scoped>
/* Aseguramos que el contenido ocupe todo el alto de la pantalla */
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
  background-color: #00B8B0;
  padding: 20px;
}

.logo-container {
  display: flex;
  align-items: center;
  gap: 10px;
}

.logo {
  width: 40px;
  height: 40px;
  background-color: white;
  color: #00B8B0;
  font-weight: bold;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px; /* Aumenta el tamaño de la fuente en el logo */
}

.title {
  color: white;
  font-size: 30px; /* Aumenta el tamaño de la fuente del título */
  font-weight: bold;
}

.main-content {
  flex-grow: 1;
  display: flex;
  justify-content: center; /* Centra todo el contenido en la pantalla */
  align-items: center;
  text-align: center;
  padding: 40px;
  background-color: white;
}

.content-container {
  display: flex; /* Usa flexbox para organizar el texto e imagen */
  justify-content: space-between; /* Separa el texto a la izquierda y la imagen a la derecha */
  align-items: center;
  max-width: 1000px; /* Ancho máximo del contenedor */
}

.content {
  flex: 1; /* El contenido de texto toma el espacio disponible */
  padding-right: 20px; /* Espacio entre el texto y la imagen */
}

.subtitle {
  font-size: 28px; /* Aumenta el tamaño del subtítulo */
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
}

.bold-text {
  font-weight: bold;
}

.description {
  color: #555;
  font-size: 18px; /* Aumenta el tamaño de la descripción */
  margin-bottom: 30px;
}

.ingresar-button {
  background-color: #00B8B0;
  color: white;
  padding: 15px 30px; /* El tamaño del botón se mantiene igual */
  border-radius: 30px;
  font-size: 18px; /* El tamaño de la fuente del botón se mantiene igual */
  cursor: pointer;
  transition: background-color 0.3s ease;
  border: none;
}

.ingresar-button:hover {
  background-color: #009990;
}

.image-container {
  flex-shrink: 0; /* Impide que la imagen se reduzca */
}

.document-image {
  width: 300px; /* Ajusta el tamaño de la imagen */
  height: auto;
  border-radius: 8px; /* Bordes redondeados para la imagen */
}

.footer {
  background-color: #00B8B0;
  color: white;
  text-align: center;
  padding: 20px;
  font-size: 18px; /* Aumenta el tamaño de la fuente del pie de página */
}
</style>
