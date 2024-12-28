<template>
    <div class="page-container">
      <main class="main-content">
        <div class="title-content">
          <h1 class="title-content-text">Acta de constitución del proyecto</h1>
        </div>
        
        <div class="form-container">
          <form @submit.prevent="handleSubmit" class="constitution-form">
            <!-- Información básica -->
            <section class="form-section">
              <h2 class="section-title">Información Básica</h2>
              <div class="form-group">
                <label for="proyectName">Nombre del Proyecto *</label>
                <input
                  id="proyectName"
                  v-model="formData.proyectName"
                  type="text"
                  class="form-input"
                  required
                />
              </div>
  
              <div class="form-group">
                <label for="company">Empresa Encargada *</label>
                <input
                  id="company"
                  v-model="formData.company"
                  type="text"
                  class="form-input"
                  required
                />
              </div>
  
              <div class="form-row">
                <div class="form-group">
                  <label for="proyectPromotor">Promotor del Proyecto *</label>
                  <input
                    id="proyectPromotor"
                    v-model="formData.proyectPromotor"
                    type="text"
                    class="form-input"
                    required
                  />
                </div>
  
                <div class="form-group">
                  <label for="proyectLeader">Líder del Proyecto *</label>
                  <input
                    id="proyectLeader"
                    v-model="formData.proyectLeader"
                    type="text"
                    class="form-input"
                    required
                  />
                </div>
              </div>
            </section>
  
            <!-- Propósito y Descripción -->
            <section class="form-section">
              <h2 class="section-title">Propósito y Descripción</h2>
              <div class="form-group">
                <label for="proyectPurpouse">Propósito del Proyecto *</label>
                <textarea
                  id="proyectPurpouse"
                  v-model="formData.proyectPurpouse"
                  class="form-input form-textarea"
                  required
                ></textarea>
              </div>
  
              <div class="form-group">
                <label for="proyectDescription">Descripción del Proyecto *</label>
                <textarea
                  id="proyectDescription"
                  v-model="formData.proyectDescription"
                  class="form-input form-textarea"
                  required
                ></textarea>
              </div>
            </section>
  
            <!-- Objetivos -->
            <section class="form-section">
              <h2 class="section-title">Objetivos</h2>
              <div class="form-group">
                <label for="descriptionObjectives">Descripción de Objetivos *</label>
                <textarea
                  id="descriptionObjectives"
                  v-model="formData.descriptionObjectives"
                  class="form-input form-textarea"
                  required
                ></textarea>
              </div>
  
              <div class="form-row">
                <div class="form-group">
                  <label for="objectivesMission">Misión *</label>
                  <textarea
                    id="objectivesMission"
                    v-model="formData.objectivesMission"
                    class="form-input form-textarea"
                    required
                  ></textarea>
                </div>
  
                <div class="form-group">
                  <label for="objectivesVision">Visión *</label>
                  <textarea
                    id="objectivesVision"
                    v-model="formData.objectivesVision"
                    class="form-input form-textarea"
                    required
                  ></textarea>
                </div>
              </div>
  
              <div class="form-group">
                <label for="objectivesValues">Valores *</label>
                <textarea
                  id="objectivesValues"
                  v-model="formData.objectivesValues"
                  class="form-input form-textarea"
                  required
                ></textarea>
              </div>
  
              <div class="form-group">
                <label for="objectivesSpecific">Objetivos Específicos *</label>
                <textarea
                  id="objectivesSpecific"
                  v-model="formData.objectivesSpecific"
                  class="form-input form-textarea"
                  required
                ></textarea>
              </div>
            </section>
  
            <!-- Alcance y Entregables -->
            <section class="form-section">
              <h2 class="section-title">Alcance y Entregables</h2>
              <div class="form-group">
                <label for="preliminaryScope">Alcance Preliminar *</label>
                <textarea
                  id="preliminaryScope"
                  v-model="formData.preliminaryScope"
                  class="form-input form-textarea"
                  required
                ></textarea>
              </div>
  
              <div class="form-group">
                <label for="deliverables">Entregables *</label>
                <textarea
                  id="deliverables"
                  v-model="formData.deliverables"
                  class="form-input form-textarea"
                  required
                ></textarea>
              </div>
            </section>
  
            <!-- Riesgos y Criterios de Éxito -->
            <section class="form-section">
              <h2 class="section-title">Riesgos y Criterios de Éxito</h2>
              <div class="form-group">
                <label for="preliminaryRisks">Riesgos Preliminares *</label>
                <textarea
                  id="preliminaryRisks"
                  v-model="formData.preliminaryRisks"
                  class="form-input form-textarea"
                  required
                ></textarea>
              </div>
  
              <div class="form-group">
                <label for="successCriteria">Criterios de Éxito *</label>
                <textarea
                  id="successCriteria"
                  v-model="formData.successCriteria"
                  class="form-input form-textarea"
                  required
                ></textarea>
              </div>
            </section>
  
            <!-- Roles y Requisitos -->
            <section class="form-section">
              <h2 class="section-title">Roles y Requisitos</h2>
              <div class="form-group">
                <label for="rolesAndResponsabilities">Roles y Responsabilidades *</label>
                <textarea
                  id="rolesAndResponsabilities"
                  v-model="formData.rolesAndResponsabilities"
                  class="form-input form-textarea"
                  required
                ></textarea>
              </div>
  
              <div class="form-group">
                <label for="descriptionRequirements">Descripción de Requisitos *</label>
                <textarea
                  id="descriptionRequirements"
                  v-model="formData.descriptionRequirements"
                  class="form-input form-textarea"
                  required
                ></textarea>
              </div>
  
              <div class="form-row">
                <div class="form-group">
                  <label for="functionalRequirements">Requisitos Funcionales *</label>
                  <textarea
                    id="functionalRequirements"
                    v-model="formData.functionalRequirements"
                    class="form-input form-textarea"
                    required
                  ></textarea>
                </div>
  
                <div class="form-group">
                  <label for="nonFunctionalRequirements">Requisitos No Funcionales *</label>
                  <textarea
                    id="nonFunctionalRequirements"
                    v-model="formData.nonFunctionalRequirements"
                    class="form-input form-textarea"
                    required
                  ></textarea>
                </div>
              </div>
            </section>
  
            <div class="form-actions">
              <button 
                type="button" 
                class="cancel-button"
                @click="$router.push('/dashboard')"
              >
                Cancelar
              </button>
              <button 
                type="submit" 
                class="submit-button"
                :disabled="loading"
              >
                {{ loading ? 'Creando...' : 'Crear Acta' }}
              </button>
            </div>
          </form>
  
          <div v-if="errorMessage" class="error-message">
            {{ errorMessage }}
          </div>
        </div>
      </main>
    </div>
  </template>
  
  <script setup>
  import { ref, reactive, onMounted } from 'vue';
  import { useCookie } from 'nuxt/app';
  
  const loading = ref(false);
  const errorMessage = ref('');
  
  const formData = reactive({
    proyectName: '',
    company: '',
    proyectPromotor: '',
    proyectLeader: '',
    elaborationDate: new Date().toISOString().split('T')[0],
    proyectPurpouse: '',
    proyectDescription: '',
    descriptionObjectives: '',
    objectivesMission: '',
    objectivesVision: '',
    objectivesValues: '',
    objectivesSpecific: '',
    preliminaryScope: '',
    deliverables: '',
    preliminaryRisks: '',
    successCriteria: '',
    rolesAndResponsabilities: '',
    descriptionRequirements: '',
    functionalRequirements: '',
    nonFunctionalRequirements: ''
  });
  
  const fetchProjectData = async () => {
    try {
      loading.value = true;
      errorMessage.value = '';
  
      const userIdCookie = useCookie('userId');
      const tokenCookie = useCookie('authToken');
      const projectIdCookie = useCookie('projectId');
  
      if (!userIdCookie.value || !tokenCookie.value || !projectIdCookie.value) {
        alert('ALERTA: ¡Sesión no iniciada o proyecto no seleccionado!, redirigiendo a login...')
        await navigateTo('/login')
        return;
      }
  
      const response = await fetch(`http://localhost:8080/api/v1/proyect/getById/${projectIdCookie.value}`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${tokenCookie.value}`,
          'Content-Type': 'application/json',
        }
      });
  
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Error al obtener la información del proyecto');
      }
  
      const projectData = await response.json();
      
      // Actualizar el formData con la información del proyecto
      formData.proyectName = projectData.nameProyect || '';
      formData.company = projectData.organization || '';
      formData.elaborationDate = projectData.startDate || new Date().toISOString().split('T')[0];
      formData.proyectDescription = projectData.description || '';
      // Aquí puedes mapear otros campos que vengan de la API
      
    } catch (error) {
      console.error('Error fetching project data:', error);
      errorMessage.value = error.message || 'Error al obtener la información del proyecto. Por favor, intenta nuevamente.';
    } finally {
      loading.value = false;
    }
  };
  
  onMounted(() => {
    fetchProjectData();
  });
  
  const handleSubmit = async () => {
    try {
      loading.value = true;
      errorMessage.value = '';
  
      const userIdCookie = useCookie('userId');
      const tokenCookie = useCookie('authToken');
  
      if (!userIdCookie.value || !tokenCookie.value) {
        alert('ALERTA: ¡Sesión no iniciada!, redirigiendo a login...')
        await navigateTo('/login')
        return;
      }
  
      const response = await fetch('http://localhost:8080/api/v1/constitution/create', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${tokenCookie.value}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          ...formData,
          idUsuario: parseInt(userIdCookie.value),
        }),
      });
  
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Error al crear el acta de constitución');
      }
  
      alert('Acta de constitución creada exitosamente, redirigiendo al Dashboard...');
      await navigateTo('/dashboard')
    } catch (error) {
      console.error('Error creating constitution act:', error);
      errorMessage.value = error.message || 'Error al crear el acta de constitución. Por favor, intenta nuevamente.';
    } finally {
      loading.value = false;
    }
  };
  </script>
  
  <style scoped>
  .page-container {
    min-height: 100vh;
    background-color: #f5f5f5;
  }
  
  .main-content {
    padding: 2rem;
  }
  
  .title-content {
    background-color: #f5f5f5;
    padding: 1.5rem 3rem;
  }
  
  .title-content-text {
    max-width: 800px;
    margin: 0 auto;
    color: #000000;
    font-size: 1.5rem;
  }
  
  .form-container {
    max-width: 800px;
    margin: 2rem auto;
    background: white;
    padding: 2rem;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
  
  .constitution-form {
    display: flex;
    flex-direction: column;
    gap: 2rem;
  }
  
  .form-section {
    border-bottom: 1px solid #eee;
    padding-bottom: 2rem;
    margin-bottom: 2rem;
  }
  
  .section-title {
    font-size: 1.25rem;
    color: #000000;
    margin-bottom: 1.5rem;
  }
  
  .form-group {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
    margin-bottom: 1.5rem;
  }
  
  .form-row {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 1.5rem;
  }
  
  label {
    font-weight: 500;
    color: #333;
  }
  
  .form-input {
    padding: 0.75rem;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 1rem;
    transition: border-color 0.3s;
    color: #000000;
    background-color: #ffffff;
  }
  
  .form-input:focus {
    outline: none;
    border-color: #00B8B0;
    box-shadow: 0 0 0 2px rgba(0, 184, 176, 0.1);
  }
  
  .form-textarea {
    resize: vertical;
    min-height: 100px;
  }
  
  .form-actions {
    display: flex;
    gap: 1rem;
    justify-content: flex-end;
    margin-top: 2rem;
    padding-top: 2rem;
    border-top: 1px solid #eee;
  }
  
  .submit-button, .cancel-button {
    padding: 0.75rem 1.5rem;
    border-radius: 4px;
    font-size: 1rem;
    cursor: pointer;
    transition: all 0.3s;
  }
  
  .submit-button {
    background-color: #00B8B0;
    color: white;
    border: none;
  }
  
  .submit-button:hover:not(:disabled) {
    background-color: #009B94;
  }
  
  .submit-button:disabled {
    opacity: 0.7;
    cursor: not-allowed;
  }
  
  .cancel-button {
    background-color: #f5f5f5;
    color: #666;
    border: 1px solid #ddd;
  }
  
  .cancel-button:hover {
    background-color: #eee;
  }
  
  .error-message {
    margin-top: 1rem;
    padding: 1rem;
    border-radius: 4px;
    background-color: #ffe6e6;
    color: #dc3545;
    text-align: center;
  }
  
  @media (max-width: 768px) {
    .form-container {
      padding: 1rem;
    }
    
    .form-row {
      grid-template-columns: 1fr;
    }
    
    .form-actions {
      flex-direction: column-reverse;
    }
    
    .submit-button, .cancel-button {
      width: 100%;
    }
    
    .section-title {
      font-size: 1.1rem;
    }
  }
  </style>