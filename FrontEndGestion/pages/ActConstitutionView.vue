<template>
  <div class="page-container">
    <header class="header">
      <div class="logo-container">
        <img src="../assets/usach-logo.png" alt="USACH" class="usach-logo" />
      </div>
    </header>
    
    <main class="main-content">
      <div class="title-section">
        <h1 class="main-title">Acta de Constitución del Proyecto</h1>
      </div>
      
      <div v-if="pdfUrl" class="document-container">
        <div class="document-header">
          <h2 class="document-title">Vista previa del documento</h2>
          <div class="document-actions">
            <a :href="pdfUrl" download="ActaConstitucion.pdf" class="action-button download-button">
              <span class="button-icon">
                <svg xmlns="http://www.w3.org/2000/svg" class="icon" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4" />
                </svg>
              </span>
              Descargar PDF
            </a>
          </div>
        </div>
        
        <div class="document-viewer">
          <iframe :src="pdfUrl" class="pdf-display"></iframe>
        </div>
        
        <div class="document-footer">
          <button @click="navigateTo('/ProjectManagementAreas')" class="action-button return-button">
            Volver al Dashboard
          </button>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { useProjectStore } from '../stores/project'

const router = useRouter()
const AuthStore = useAuthStore()
const ProjectStore = useProjectStore()
const pdfUrl = ref(null)

const loadPdf = async () => {
  try {
    const token = AuthStore.token
    const projectId = ProjectStore.projectId

    if (!token || !projectId) {
      console.error('No hay token o projectId')
      router.push('/login')
      return
    }

    const response = await fetch(
      `http://localhost:8080/api/v1/exit/actInstitution/ByProyectId/${projectId}`,
      {
        headers: {
          'Authorization': `Bearer ${token}`,
        }
      }
    )

    if (!response.ok) {
      throw new Error('Error al obtener el PDF')
    }

    const blob = await response.blob()
    pdfUrl.value = URL.createObjectURL(blob)
  } catch (error) {
    console.error('Error:', error)
  }
}

const navigateTo = (route) => {
  router.push(route)
}

onMounted(() => {
  loadPdf()
})
</script>

<style scoped>
.page-container {
  min-height: 100vh;
  background-color: #f8fafc;
  display: flex;
  flex-direction: column;
}

.header {
  background-color: #00B8B0;
  padding: 1rem 2rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.logo-container {
  max-width: 1200px;
  margin: 0 auto;
}

.usach-logo {
  height: 3rem;
  width: auto;
}

.main-content {
  flex: 1;
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
}

.title-section {
  margin-bottom: 2rem;
  text-align: center;
}

.main-title {
  color: #1a202c;
  font-size: 2rem;
  font-weight: 700;
  line-height: 1.2;
}

.document-container {
  background: white;
  border-radius: 1rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  padding: 2rem;
  margin: 0 auto;
  max-width: 900px;
}

.document-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid #e2e8f0;
}

.document-title {
  font-size: 1.5rem;
  color: #2d3748;
  font-weight: 600;
  margin: 0;
}

.document-actions {
  display: flex;
  gap: 1rem;
}

.document-viewer {
  background: #f7fafc;
  border-radius: 0.5rem;
  padding: 1rem;
  margin-bottom: 1.5rem;
}

.pdf-display {
  width: 100%;
  height: 750px;
  border: none;
  border-radius: 0.375rem;
  background: white;
}

.document-footer {
  display: flex;
  justify-content: flex-end;
  padding-top: 1rem;
  border-top: 1px solid #e2e8f0;
}

.action-button {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1.5rem;
  border-radius: 0.5rem;
  font-size: 1rem;
  font-weight: 500;
  transition: all 0.2s ease;
  cursor: pointer;
}

.download-button {
  background-color: #00B8B0;
  color: white;
  text-decoration: none;
  border: none;
}

.download-button:hover {
  background-color: #009B94;
  transform: translateY(-1px);
}

.return-button {
  background-color: #e2e8f0;
  color: #4a5568;
  border: none;
}

.return-button:hover {
  background-color: #cbd5e0;
  transform: translateY(-1px);
}

.button-icon {
  display: inline-flex;
}

.icon {
  width: 1.25rem;
  height: 1.25rem;
}

@media (max-width: 768px) {
  .main-content {
    padding: 1rem;
  }

  .document-container {
    padding: 1.5rem;
    margin: 0 1rem;
  }

  .document-header {
    flex-direction: column;
    gap: 1rem;
    align-items: flex-start;
  }

  .document-actions {
    width: 100%;
  }

  .action-button {
    width: 100%;
    justify-content: center;
  }

  .pdf-display {
    height: 500px;
  }
}
</style>