<template>
    <div class="background">
        <div class="container">
          <h1 class="form-title">Plan de Gestión de Requisitos</h1>
        
          <form class="form" @submit.prevent="handleSubmit">
            <section class="form-section">
              <h2 class="section-title">Información Básica</h2>
            
              <div class="form-group">
                <label>Nombre del Proyecto *</label>
                <input v-model="formData.proyectName" class="form-input" type="text" placeholder="Ingrese el nombre del proyecto" required disabled />
              </div>
          
              <div class="flex-row">
                <div class="form-group half-width">
                  <label>Identificador del Proyecto *</label>
                  <input v-model="formData.idProyect" class="form-input" type="text" placeholder="Ingrese el identificador del proyecto" required disabled />
                </div>
            
                <div class="form-group half-width">
                  <label>Fecha de Elaboración *</label>
                  <input v-model="formData.elaborationDate" class="form-input" type="date" placeholder="yyyy-mm-dd" required />
                </div>
              </div>
          
              <div class="flex-row">
                <div class="form-group half-width">
                  <label>Líder del Proyecto *</label>
                  <input v-model="formData.proyectLeader" class="form-input" type="text" placeholder="Ingrese el líder del proyecto" required disabled />
                </div>
            
                <div class="form-group half-width">
                  <label>Líder de QA *</label>
                  <input v-model="formData.qaLeader" class="form-input" type="text" placeholder="Ingrese el líder de QA" required />
                </div>
              </div>
            </section>
        
            <section class="form-section">
              <h2 class="section-title">Requisitos</h2>
              <div class="form-group">
                <label>Identificación de Requisitos *</label>
                <textarea v-model="formData.requirementsIdentification" class="form-input form-textarea" placeholder="Describa los métodos para identificar requisitos" required></textarea>
              </div>
          
              <div class="form-group">
                <label>Análisis de Requisitos *</label>
                <textarea v-model="formData.requirementsAnalysis" class="form-input form-textarea" 
                placeholder="Haga un análisis de  los requisitos para determinar si pertenecen a las categorías de proyecto o producto." required></textarea>
              </div>
          
              <div class="form-group">
                <label>Gestión Continua de Requisitos *</label>
                <textarea v-model="formData.continuousRequirements" class="form-input form-textarea" placeholder="Explique cómo se gestionarán los cambios" required></textarea>
              </div>
          
              <div class="form-group">
                <label>Documentación y Control de Versiones *</label>
                <textarea v-model="formData.documentationAndVersionControl" class="form-input form-textarea" 
                placeholder="Describa cómo y quienes llevarán a cabo la revisión de la documentación y el control de versiones." required></textarea>
              </div>
          
              <div class="form-group">
                <label>Control de Cambios *</label>
                <textarea v-model="formData.changeControl" class="form-input form-textarea" 
                placeholder="Explique cómo y quienes llevarán a cabo el control de cambios" required></textarea>
              </div>
            </section>

            <!-- Requirements -->
            <section class="form-section">
              <label>Proceso de Priorización de Requisitos *</label>

              <div v-for="(requirement, index) in formData.requirementsPrioritization" :key="index" class="requirement-row">
                <div class="requirement-inputs">
                  <div class="form-group half-width">
                    <label :for="'requirementId' + index">Identificador *</label>
                    <input :id="'requirementId' + index" v-model="requirement.id" type="text"
                        class="form-input" placeholder="Identificador del Requisito" @input="validateNoCommas($event, 'requirementPriorization', index, 'id')" required />
                        <span v-if="fieldHadComma('requirementPriorization', index)" class="invalid-feedback">No se permiten comas en este campo</span>
                  </div>

                  <div class="form-group half-width">
                    <label :for="'requirementPriority' + index">Priorización *</label>
                    <select :id="'requirementPriority' + index" v-model="requirement.priority" class="form-input" required>
                      <option value="" disabled selected>Seleccione un tipo</option>
                      <option value="Baja">Baja</option>
                      <option value="Media">Media</option>
                      <option value="Alta">Alta</option>
                    </select>
                  </div>
                </div>

                <button v-if="formData.requirementsPrioritization.length > 1" type="button"
                    class="remove-requirement-button" @click="removeRequirement(index)">
                    ×
                </button>
              </div>

              <div class="add-requirement-container">
                <button type="button" class="add-requirement-button" @click="addRequirement">
                    +
                </button>
              </div>
            </section>
            <!-- Requirements END -->

            <section class="form-section">
              <h2 class="section-title">Criterios de Gestión</h2>
              <div class="form-group">
                <label>Costo *</label>
                <textarea v-model="formData.cost" class="form-input form-textarea" placeholder="Describa el límite de costo y la justificación de este" required></textarea>
              </div>
          
              <div class="form-group">
                <label>Calidad *</label>
                <textarea v-model="formData.quality" class="form-input form-textarea" placeholder="Describa el límite de calidad y la justificación de este" required></textarea>
              </div>
          
              <div class="form-group">
                <label>Rendimiento *</label>
                <textarea v-model="formData.performance" class="form-input form-textarea" placeholder="Describa el límite de rendimiento y la justificación de este" required></textarea>
              </div>
            </section>
        
            <div class="form-actions">
                <button type="button" class="cancel-button" @click="showCancelConfirmation = true">
                    Cancelar
                </button>
                <button type="button" class="save-button" :disabled="loading" @click="handleSave">
                    {{ loading ? 'Guardando...' : 'Guardar' }}
                </button>
                <button type="submit" class="submit-button" :disabled="loading" @click="handleSubmit">
                    {{ loading ? 'Creando...' : 'Crear Documento' }}
                </button>
            </div>

            <div v-if="showCancelConfirmation" class="modal-overlay">
                <div class="modal-content">
                    <h3 class="section-title">Confirmar Cancelación</h3>
                    <label>¿Estás seguro que deseas cancelar? Los cambios no guardados se perderán.</label>
                    <div class="modal-actions">
                        <button class="cancel-button" @click="showCancelConfirmation = false">
                            No, continuar editando
                        </button>
                        <button class="confirm-button" @click="handleCancel">
                            Sí, cancelar
                        </button>
                    </div>
                </div>
            </div>
          </form>

          <div v-if="errorMessage" class="error-message">
              {{ errorMessage }}
          </div>

          <div v-if="pdfUrl" class="pdf-container">
              <div class="pdf-header">
                  <h2 class="pdf-title">Vista previa del documento</h2>
                  <div class="pdf-actions">
                      <a :href="pdfUrl" download="PlanGestionDeRequisitos.pdf" class="pdf-button download-button">
                          <span class="button-icon">↓</span>
                          Descargar PDF
                      </a>
                  </div>
              </div>

              <div class="pdf-viewer">
                  <iframe :src="pdfUrl" class="pdf-iframe"></iframe>
              </div>

              <div class="pdf-footer">
                  <button @click="navigateTo('/dashboard')" class="pdf-button return-button">
                      Volver al Dashboard
                  </button>
              </div>

          </div>

        </div>
    </div>
  </template>
  
  <script setup>
  import { ref, reactive, onMounted } from 'vue';
  import { useAuthStore } from '../stores/auth';
  import { useProjectStore } from '../stores/project';
  
  const loading = ref(false);
  const errorMessage = ref('');
  const AuthStore = useAuthStore();
  const ProjectStore = useProjectStore();
  const pdfUrl = ref(null);
  const invalidFields = ref(new Set());
  const showCancelConfirmation = ref(false);
  const tableFieldsWithComma = ref(new Map());

  const url_doc_generator = "http://localhost:8080/api/documents/requirements-management-plan"
  const url_exit = "http://localhost:8080/api/v1/exit"
  const url_process = "http://localhost:8080/api/v1/process"
  
  const formData = reactive({
    proyectName: "",
    idProyect: "",
    proyectLeader: "",
    qaLeader: "",
    elaborationDate: new Date().toISOString().split('T')[0],
    requirementsPrioritization: [
        {
            id: '',
            priority: ''
        }
    ],
    requirementsIdentification: "",
    requirementsAnalysis: "",
    continuousRequirements: "",
    documentationAndVersionControl: "",
    changeControl: "",
    cost: "",
    quality: "",
    performance: "",
  });

  const validateForm = () => {
    invalidFields.value.clear();
    
    // Basic field validation
    Object.entries(formData).forEach(([key, value]) => {
        if (typeof value === 'string' && !value.trim()) {
            invalidFields.value.add(key);
        }
    });

    // Table validation
    formData.requirementsPrioritization.forEach((requirement, index) => {
        if (!requirement.id || !requirement.priority || hasComma(requirement.id)) {
            invalidFields.value.add(`requirement-${index}`);
        }
    });

    return invalidFields.value.size === 0;
  };

  const formatRequirementsToString = (requirements) => {
    if (!requirements || requirements.length === 0) return '';

    let result = '&Identificador del Requisito,Prioridad&';

    requirements.forEach((requirement, _) => {
        result += `${requirement.id},${requirement.priority}&`;
    });

    return result;
  };

  const handleCancel = () => {
      showCancelConfirmation.value = false;
      navigateTo('/principalView');
  };

  const addRequirement = () => {
    formData.requirementsPrioritization.push({
        id: '',
        priority: ''
    });
  };

  const removeRequirement = (index) => {
    cleanMapList('requirementPriorization'); // Remove "no comma allowed" messages to avoid desynchronization
    formData.requirementsPrioritization.splice(index, 1);
  };

  const fetchProjectData = async () => {
    try {
        loading.value = true;
        errorMessage.value = '';

        const userId = AuthStore.userId;
        const token = AuthStore.token;
        const projectId = ProjectStore.projectId;

        if (!userId || !token || !projectId) {
            alert('ALERTA: ¡Sesión no iniciada o proyecto no seleccionado!, redirigiendo a login...')
            await navigateTo('/login')
            return;
        }

        // Obtain proyect data
        const respondeProyect = await fetch(`http://localhost:8080/api/v1/proyect/getById/${projectId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            }
        });

        // Obtain user data
        const respondeUser = await fetch(`http://localhost:8080/api/v1/user/getById/${userId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            }
        });

        if (!respondeProyect.ok || !respondeUser.ok) {
            throw new Error('Error al obtener la información necesaria');
        }

        const projectDataResponse = await respondeProyect.json();
        const userDataResponse = await respondeUser.json();

        // Updating form data
        formData.proyectName = projectDataResponse.nameProyect;
        formData.idProyect = projectDataResponse.idProyecto;
        formData.elaborationDate = projectDataResponse.startDate;
        formData.proyectLeader = `${userDataResponse.names} ${userDataResponse.secondNames}`;

    } catch (error) {
        console.error('Error fetching data:', error);
        errorMessage.value = 'Error al obtener la información. Por favor, intenta nuevamente.';
    } finally {
        loading.value = false;
    }
  };

  onMounted(() => {
    fetchProjectData();
  });


  //----- COMMA VALIDATION AND ERROR MESSAGE CONTROL -----
  const hasComma = (value) => {
    return value && value.includes(',');
  };
  
  const validateNoCommas = (event, type, index, field) => {
      const value = event.target.value;
      if (value.includes(',')) {
        // Delete commas
        if (type === 'requirementPriorization') {
          addItemToListInMap('requirementPriorization', index);
          formData.requirementsPrioritization[index][field] = value.replace(/,/g, '');
        }
      } else {
        if (type === 'requirementPriorization') {
          removeItemFromMapList('requirementPriorization', index);
        }
      }
  };

  const addItemToListInMap = (key, item) => {
    if (tableFieldsWithComma.value.has(key)) {
      if (! tableFieldsWithComma.value.get(key).includes(item)){
        tableFieldsWithComma.value.get(key).push(item);
      }
    } else {
      tableFieldsWithComma.value.set(key, [item]);
    }
  };

  const removeItemFromMapList = (key, item) => {
    if (tableFieldsWithComma.value.has(key)) {
      const list = tableFieldsWithComma.value.get(key);
      const index = list.indexOf(item);
      if (index > -1) {
        list.splice(index, 1);
      }
    }
  };

  const cleanMapList = (key) => {
    tableFieldsWithComma.value.set(key, []);
  }

  const fieldHadComma = (key, item) => {
    if (tableFieldsWithComma.value.has(key)) {
      return tableFieldsWithComma.value.get(key).includes(item)
    }
  };

  //----- END OF COMMA VALIDATION AND ERROR MESSAGE CONTROL -----

  const handleSave = () => {
    const formattedRequirementPriorization = formatRequirementsToString(formData.requirementsPrioritization);

    const formattedData = {
        ...formData,
        requirementsPrioritization: formattedRequirementPriorization,
    };

    console.log('Datos del formulario (raw):', formData);
    console.log('Datos formateados para envío:', formattedData);
    console.log('Requirement Priorizations formateados:', formattedRequirementPriorization);
    console.log('JSON completo:', JSON.stringify(formattedData, null, 2));
  };

  const handleSubmit = async () => {
    if (!validateForm()) {
        errorMessage.value = 'Por favor, complete todos los campos obligatorios';
        return;
    }
    
    try {
        loading.value = true;
        errorMessage.value = '';

        const userId = AuthStore.userId;
        const token = AuthStore.token;
        const projectId = ProjectStore.projectId;

        if (!userId || !token || !projectId) {
            alert('ALERTA: ¡Sesión no iniciada!, redirigiendo a login...')
            await navigateTo('/login')
            return;
        }

        // Generating respective Exit in backend
        const responseExit = await fetch(url_exit + "/create", {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                nameExit: "Plan de Gestion de Requisitos",
                description: "Plan donde se explican los metodos y herramientas para gestionar los requisitos",
                dateCreation: formData.elaborationDate,
                priority: "Alta",
                responsible: formData.proyectLeader,
                state: "Activo",
                idProcess: "", // FALTA RECUPERAR DE UN STORE
            }),
        });

        if (!responseExit.ok) {
            const errorData = await responseExit.json();
            throw new Error(errorData.message || 'Error al crear la salida: Plan de Gestión de Requisitos');
        }
        const exitResponse = await responseExit.json();

        const formattedRequirementsPrioritization = formatRequirementsToString(formData.requirementsPrioritization);

        // Endpoint call to generate pdf document
        const responseRequirementManagmentPlan = await fetch(url_doc_generator + `/generate?idExit=${exitResponse.idExit}`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                ...formData,
                requirementsPrioritization: formattedRequirementsPrioritization,
            }),
        });

        if (!responseRequirementManagmentPlan.ok) {
            const errorData = await responseRequirementManagmentPlan.json();
            throw new Error(errorData.message || 'Error al crear el Plan de Gestión de Requisitos');
        }

        const pdfBlob = await responseRequirementManagmentPlan.blob();
        pdfUrl.value = URL.createObjectURL(pdfBlob);

    } catch (error) {
        console.error('Error creating requirement managment plan:', error);
        errorMessage.value = error.message || 'Error al crear el Plan de Gestión de Requisitos. Por favor, intenta nuevamente.';
    } finally {
        loading.value = false;
    }
  };
  </script>
  
  <style scoped>

  .background {
    background-color: #F5F5F5;
    width: 100%;
    min-height: 100vh; /* Ensure it covers the full viewport */
    display: flex;
    align-items: center; /* Center the form vertically */
    justify-content: center; /* Center the form horizontally */
    box-sizing: border-box; /* Include padding in width/height calculations */
  }

  .container {
    width: 100%;
    max-width: 900px;
    margin: auto;
    background-color: #ffffff !important;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    margin-top: 150px; /* Moves the form below the header */
    margin-bottom: 5%; /* Moves the form below the header */
  }
  
  .form-title {
    text-align: center;
    margin-bottom: 20px;
    font-size: 1.8rem;
    color: #333;
  }
  
  .form-section {
    margin-bottom: 20px;
  }
  
  .section-title {
    font-size: 1.5rem;
    margin-bottom: 10px;
    margin-left: 15px;
    margin-right: 15px;
    color: #444;
  }
  
  .form-group {
    margin-bottom: 15px;
    margin-left: 15px;
    margin-right: 15px;
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
    margin-bottom: 1.5rem;
  }
  
  .flex-row {
    display: flex;
    gap: 20px;
  }
  
  .half-width {
    flex: 1;  /* Ensure each field takes half the available width */
  }

  .requirement-row {
      margin-bottom: 1.5rem;
      background-color: #ffffff;
      padding: 1rem;
      border-radius: 4px;
      box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  }
  
  .requirement-inputs {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 1rem;
      margin-bottom: 0.5rem;
  }
  
  .add-requirement-container {
      display: flex;
      justify-content: center;
      margin-top: 1.5rem;
  }
  
  .add-requirement-button {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 40px;
      height: 40px;
      border-radius: 50%;
      background-color: #00B8B0;
      color: white;
      border: none;
      cursor: pointer;
      font-size: 1.5rem;
      padding: 0;
      transition: background-color 0.3s;
  }
  
  .add-requirement-button:hover {
      background-color: #009B94;
  }
  
  .remove-requirement-button {
      position: absolute;
      right: 1rem;
      top: 1rem;
      width: 24px;
      height: 24px;
      border-radius: 50%;
      background-color: #dc3545;
      color: white;
      border: none;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 1rem;
      padding: 0;
  }
  
  .remove-requirement-button:hover {
      background-color: #c82333;
  }
  
  .requirement-row {
      position: relative;
  }
  
  label {
    display: block;
    font-size: 1rem;
    margin-bottom: 5px;
    color: #555;
  }
  
  input,
  textarea {
    width: 100%;
    padding: 10px;
    font-size: 1rem;
    border: 1px solid #ddd;
    border-radius: 5px;
    background-color: #fff;
    color: #555;
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
  
  input[type="date"].form-input {
    color: #000000;
    background-color: #ffffff;
    cursor: pointer;
    position: relative;
    padding: 0.75rem;
  }
  
  input[type="date"]::-webkit-calendar-picker-indicator {
    opacity: 1;
    display: block;
    background: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='24' height='24' viewBox='0 0 24 24' fill='none' stroke='%23666' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Crect x='3' y='4' width='18' height='18' rx='2' ry='2'%3E%3C/rect%3E%3Cline x1='16' y1='2' x2='16' y2='6'%3E%3C/line%3E%3Cline x1='8' y1='2' x2='8' y2='6'%3E%3C/line%3E%3Cline x1='3' y1='10' x2='21' y2='10'%3E%3C/line%3E%3C/svg%3E") no-repeat center;
    width: 20px;
    height: 20px;
    cursor: pointer;
  }
  
  .form-input:focus {
    outline: none;
    border-color: #00B8B0;
    box-shadow: 0 0 0 2px rgba(0, 184, 176, 0.1);
  }

  .form-input:disabled {
    color: #007973;
  }
  
  .form-textarea {
    resize: vertical;
    min-height: 100px;
  }
  
  textarea {
    resize: vertical;
  }
  
  /*
  .submit-button {
    background: #007bff;
    color: #fff;
    padding: 10px 20px;
    font-size: 1rem;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
  
  .submit-button:hover {
    background: #0056b3;
  }
  */
  /*NEW STYLES*/

.submit-button,
.cancel-button {
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

.form-actions {
    display: flex;
    gap: 1rem;
    justify-content: flex-end;
    margin-top: 2rem;
    padding-top: 2rem;
    border-top: 1px solid #eee;
}

.save-button {
    padding: 0.75rem 1.5rem;
    border-radius: 4px;
    font-size: 1rem;
    cursor: pointer;
    transition: all 0.3s;
    background-color: #4CAF50;
    color: white;
    border: none;
}

.save-button:hover:not(:disabled) {
    background-color: #45a049;
}

.save-button:disabled {
    opacity: 0.7;
    cursor: not-allowed;
}

.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
}

.modal-content {
    background: white;
    padding: 2rem;
    border-radius: 8px;
    max-width: 500px;
    width: 90%;
}

.modal-content h3 {
    margin-top: 0;
    margin-bottom: 1rem;
}

.modal-actions {
    display: flex;
    justify-content: flex-end;
    gap: 1rem;
    margin-top: 2rem;
}

.confirm-button {
    padding: 0.75rem 1.5rem;
    border-radius: 4px;
    font-size: 1rem;
    cursor: pointer;
    transition: all 0.3s;
    background-color: #dc3545;
    color: white;
    border: none;
}

.confirm-button:hover {
    background-color: #c82333;
}

.pdf-container {
    margin-top: 2rem;
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    padding: 1.5rem;
}

.pdf-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1rem;
    padding-bottom: 1rem;
    border-bottom: 1px solid #eee;
}

.pdf-title {
    font-size: 1.25rem;
    color: #333;
    margin: 0;
}

.pdf-actions {
    display: flex;
    gap: 1rem;
}

.pdf-viewer {
    background: #f5f5f5;
    padding: 1rem;
    border-radius: 4px;
    margin-bottom: 1rem;
}

.pdf-iframe {
    width: 100%;
    height: 700px;
    border: none;
    border-radius: 4px;
}

.pdf-footer {
    display: flex;
    justify-content: flex-end;
    gap: 1rem;
    padding-top: 1rem;
    border-top: 1px solid #eee;
}

.pdf-button {
    padding: 0.75rem 1.5rem;
    border-radius: 4px;
    font-size: 1rem;
    cursor: pointer;
    transition: all 0.3s;
    display: flex;
    align-items: center;
    gap: 0.5rem;
}

.download-button {
    background-color: #00B8B0;
    color: white;
    text-decoration: none;
    border: none;
}

.download-button:hover {
    background-color: #009B94;
}

.return-button {
    background-color: #f5f5f5;
    color: #666;
    border: 1px solid #ddd;
}

.return-button:hover {
    background-color: #eee;
}

.button-icon {
    font-size: 1.2rem;
}

@media (max-width: 768px) {
    .stakeholder-inputs {
        grid-template-columns: 1fr;
    }
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

    .submit-button,
    .cancel-button {
        width: 100%;
    }

    .section-title {
        font-size: 1.1rem;
    }

    .stakeholder-row {
        margin-bottom: 1rem;
    }

    .stakeholder-inputs {
        display: grid;
        grid-template-columns: repeat(3, 1fr) auto;
        gap: 1rem;
        align-items: flex-end;
        margin-bottom: 1rem;
    }

    .remove-stakeholder-button {
        padding: 0.75rem 1rem;
        background-color: #dc3545;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        height: 42px;
        margin-bottom: 8px;
    }

    .remove-stakeholder-button:hover {
        background-color: #c82333;
    }

    .add-stakeholder-container {
        margin-top: 1rem;
    }

    .add-stakeholder-button {
        padding: 0.75rem 1.5rem;
        background-color: #28a745;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        display: flex;
        align-items: center;
        gap: 0.5rem;
    }

    .add-stakeholder-button:hover {
        background-color: #218838;
    }

    @media (max-width: 768px) {
        .stakeholder-inputs {
            grid-template-columns: 1fr;
        }

        .remove-stakeholder-button {
            width: 100%;
            margin-top: 0.5rem;
        }
    }
}

.invalid-input {
    border-color: #dc3545;
    background-color: #fff8f8;
}

.invalid-input:focus {
    border-color: #dc3545;
    box-shadow: 0 0 0 2px rgba(220, 53, 69, 0.1);
}

.invalid-section {
    border: 1px solid #dc3545;
    padding: 0.5rem;
    border-radius: 4px;
    background-color: #fff8f8;
}

.form-input.invalid-input::placeholder {
    color: #dc3545;
}

/* Mensaje de error para campos inválidos */
.invalid-feedback {
    color: #dc3545;
    font-size: 0.875rem;
    margin-top: 0.25rem;
}
  </style>
  