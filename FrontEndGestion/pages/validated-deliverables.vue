<template>
    <div class="page-container">
        <main class="main-content">
            <div class="title-content">
                <h1 class="title-content-text">Revisión de Desempeño del Trabajo</h1>
            </div>

            <div class="form-container">
                <div class="title-content">
                    <h1 class="title-content-text">Entregables Validados</h1>
                </div>

                <div class="text-container">
                    <p class="text-block">
                        Los entregables validados son productos, servicios o resultados que han sido revisados y confirmados como conformes a los requisitos especificados del proyecto.
                    </p>
                </div>

                <div class="table-container">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th class="id-column">ID</th>
                                <th class="name-column">Nombre del Entregable</th>
                                <th class="criteria-column">Criterios de Aceptación</th>
                                <th class="date-column">Fecha de Aceptación</th>
                                <th class="action-column">Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="item in formData.deliverableCriteria" :key="item.code">
                                <td class="id-column">{{ item.code }}</td>
                                <td class="name-column">{{ item.name }}</td>
                                <td class="criteria-column">
                                    <div class="criteria-content">{{ item.criteria }}</div>
                                </td>
                                <td class="date-column">{{ item.validationDate }}</td>
                                <td class="action-column">
                                    <!--
                                    <button @click="editDeliverable(item.code)" class="edit-button">
                                        <span class="edit-icon">✎</span>
                                    </button>
                                    -->

                                    <button @click="deleteDeliverable(item.code)" class="delete-button">
                                        <span class="delete-icon">&times;</span>
                                    </button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <br/>
                    <br/>
                    <br/>
                </div>
                
                <button type="button" @click="openPopup" class="submit-button"><strong>Agregar Entregable</strong></button>
            </div>

            <div class="form-container">
                <form @submit.prevent="handleSubmit" class="performanceReview-form">
                    <!-- Botones de acción -->
                    <section class="form-actions">
                        <button type="button" class="cancel-button" @click="showCancelConfirmation = true">
                            Cancelar
                        </button>
                        <button type="button" class="save-button" :disabled="loading"
                            @click="showSaveConfirmation = true">
                            {{ loading ? 'Guardando...' : 'Guardar' }}
                        </button>
                    </section>
                </form>

                <!-- Diálogos de confirmación -->
                <div v-if="showCancelConfirmation" class="modal-overlay">
                    <div class="modal-content">
                        <h3>Confirmar Cancelación</h3>
                        <p>¿Estás seguro que deseas cancelar? Los cambios no guardados se perderán.</p>
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

                <div v-if="showSaveConfirmation" class="modal-overlay">
                    <div class="modal-content">
                        <h3>Confirmar Guardado</h3>
                        <p>¿Estás seguro que deseas guardar los cambios actuales?</p>
                        <div class="modal-actions">
                            <button class="cancel-button" @click="showSaveConfirmation = false">
                                No, seguir editando
                            </button>
                            <button class="save-button" @click="confirmSave">
                                Sí, guardar
                            </button>
                        </div>
                    </div>
                </div>

                <div v-if="showPopup" class="popup-overlay">
                    <div class="popup">
                        <button @click="closePopup" class="close-button">&times;</button>
                        <form @submit.prevent="addDeliverable" class="validation-form">
                            <section class="form-section">
                                <h2 class="section-title">{{ isEditing ? 'Editar ENT' : 'Nueva ENT' }}</h2>
                                <div class="form-group">
                                    <label for="name">Nombre del Entregable <span class="red-text">*</span></label>
                                    <input 
                                    id="name" 
                                    class="form-input"
                                    v-model="currentDeliverable.name"
                                    @input="e => filterPipeCharacter(e, 'name')"
                                    required
                                    />
                                    <span v-if="hasPipe(currentDeliverable.name)" class="invalid-feedback">No se permiten "|" en este campo</span>
                                </div>
                                <div class="form-group">
                                    <label for="criteria">Criterios de Aceptación del Entregable <span class="red-text">*</span></label>
                                    <textarea 
                                    id="criteria" 
                                    class="form-input form-textarea" 
                                    v-model="currentDeliverable.criteria"
                                    @input="e => filterPipeCharacter(e, 'criteria')"
                                    required
                                    ></textarea>
                                    <span v-if="hasPipe(currentDeliverable.criteria)" class="invalid-feedback">No se permiten "|" en este campo</span>
                                </div>
                                <div class="form-group">
                                    <label for="validationDate">Fecha de Aceptación <span class="red-text">*</span></label>
                                    <input 
                                    id="validationDate" 
                                    v-model="currentDeliverable.validationDate" 
                                    type="date"
                                    class="form-input" 
                                    required
                                    />
                                </div>
                            </section>
                            <button class="submit-button" @click="addDeliverable"><strong>Guardar y Cerrar</strong></button>
                        </form>
                    </div>
                </div>
                <AlertPopup :show="alert.show" :title="alert.title" :message="alert.message" :type="alert.type"
                    @confirm="handleAlertConfirm" />
            </div>
        </main>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useAuthStore } from '../stores/auth';
import { useProjectStore } from '../stores/project';
import { useProcessStore } from '../stores/Process';
import { useExitStore } from '../stores/Exit';
import AlertPopup from '../components/AlertPopup.vue';
import { useRouter } from 'vue-router';
import { ro } from 'vuetify/locale';

// Store instantiation
const AuthStore = useAuthStore();
const ProjectStore = useProjectStore();
const ProcessStore = useProcessStore();
const ExitStore = useExitStore();
const router = useRouter();

// Reactive state
const loading = ref(false);
const errorMessage = ref('');
const showCancelConfirmation = ref(false);
const showSaveConfirmation = ref(false);
const showPopup = ref(false);
const isEditing = ref(false);
const showTraceabilityPopup = ref(false);
const nextCode = ref(1);

// Initial form data
const formData = reactive({
    lastENT: 0,
    deliverableCriteria: []
});

const currentDeliverable = reactive({
    code: '',
    name: '',
    criteria: '',
    validationDate: ''
})

const alert = reactive({
    show: false,
    title: '',
    message: '',
    type: 'info',
});

function openPopup() {
    resetForm();
    isEditing.value = false;
    showPopup.value = true;
}

const openEditPopup = (item) => {
    isEditing.value = true;
    currentDeliverable.code = item.code;
    currentDeliverable.name = item.name;
    currentDeliverable.criteria = item.criteria;
    currentDeliverable.validationDate = item.validationDate;
    showPopup.value = true;
}
  
function closePopup() {
    showPopup.value = false;
    resetForm();
}

function openTraceabilityPopup() {
    showTraceabilityPopup.value = true;
}
  
function closeTraceabilityPopup() {
    showTraceabilityPopup.value = false;
}

// API Functions
const createNewExit = async (processId, nameUser, token) => {
    const createExitResponse = await fetch(`http://localhost:8080/api/v1/exit/create`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            idProcess: processId,
            nameExit: 'Entregables validados',
            state: 'Activo',
            dateCreation: new Date().toISOString().split('T')[0],
            dateValidation: new Date().toISOString().split('T')[0],
            priority: 'Alta',
            responsible: nameUser,
            description: 'Entregables validados'
        })
    });

    const responseText = await createExitResponse.text();
    if (!responseText.trim()) {
        throw new Error('No se recibió ningún contenido JSON');
    }

    if (!createExitResponse.ok) {
        throw new Error(`Error al crear la salida: ${responseText}`);
    }

    const newExit = JSON.parse(responseText);
    return newExit.idExit;
};

const getExistingExit = async (processId, token) => {
    const response = await fetch(
        `http://localhost:8080/api/v1/exit/getByIdProcessAndNameExit?idProcess=${processId}&nameExit=Entregables validados`,
        {
            headers: { 'Authorization': `Bearer ${token}` },
        }
    );

    if (response.ok) {
        const textData = await response.text();
        if (!textData.trim()) {
            return null; // Indicate need to create new exit
        }
        const exitData = JSON.parse(textData);
        return exitData.idExit;
    }
    return null;
};

const invalidFields = ref(new Set());

const validateForm = () => {
    invalidFields.value.clear();

    // Validar campos
    const requiredFields = ['name', 'criteria', 'validationDate'];
  
    // Solo validar los campos requeridos
    requiredFields.forEach(field => {
        if (!currentDeliverable[field]?.trim()) {
        invalidFields.value.add(field);
        }
    });

    // Validar caracteres pipe
    if (hasPipe(currentDeliverable.name) || hasPipe(currentDeliverable.criteria)) {
        invalidFields.value.add('pipe');
    }

    return invalidFields.value.size === 0;
};

const isFieldInvalid = (fieldName) => {
    return false; 
};

const handleCancel = () => {
    showCancelConfirmation.value = false;
    navigateTo('/principalView');
};

const handleEverything = async () => {
    if (isEditing.value) {
        // Actualizar el entregable existente
        const index = formData.deliverableCriteria.findIndex(
        item => item.code === currentDeliverable.code
        )
        if (index !== -1) {
            formData.deliverableCriteria[index] = { ...currentDeliverable }
        }
    } else {
        // Agregar nuevo entregable
        const newDeliverable = {
        ...currentDeliverable,
        code: generateNewCode() // Asumiendo que tienes esta función
        }
        formData.deliverableCriteria.push(newDeliverable)
    }
    closePopup()
};

onMounted(async () => {
    try {
        loading.value = true;
        const projectId = ProjectStore.projectId;
        const userId = AuthStore.userId;
        const token = AuthStore.token;

        // Obtener el exitId
        const exitId = await getExistingExit(ProcessStore.processId, token);

        if (exitId) {
            // Segunda peticion
            const parametersResponse = await fetch(
                `http://localhost:8080/api/v1/exit/${exitId}/parameters`,
                {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    }
                }
            );

            if (parametersResponse.ok) {
                const parameters = await parametersResponse.json();

                // Mapear los parametros a los campos del formulario
                parameters.forEach(param => {
                    switch (param.nameParameter) {
                        case 'lastENT':
                            formData.lastENT = param.content;
                            console.log(`Processing parameter: ${param.nameParameter}`, param.content);
                            break;
                        case 'deliverableCriteria':
                            const criterias = parseCriterias(param.content);
                            formData.deliverableCriteria = [...criterias];
                            console.log(`Processing parameter: ${param.nameParameter}`, param.content);
                            console.log('Parsed Criterias:', formData.deliverableCriteria);
                            break;
                        default:
                            break;
                    }                    
                });
            }
        }
    } catch (error) {
        showAlert('Error', 'Error al cargar los datos del proyecto', 'error');
        console.error('Error:', error);
    } finally {
        loading.value = false;
    }
});

const parseCriterias = (content) => {
    try {
        const rows = content.split('&');
        // Ignorar la fila de encabezados y el ultimo elemento vacio
        const dataRows = rows.slice(2,-1);

        if (dataRows.lenght === 0) {
            return [{
                id: '',
                description: ''
            }];
        }

        return dataRows.map(row => {
            const [code, name, criteria, validationDate] = row.split('|');
            return {
                code: code || '',
                name: name || '',
                criteria: criteria || '',
                validationDate: validationDate || ''
            };
        });
    } catch (error) {
        console.error('Error parsing criterias:', error);
        return [{
            code: code || '',
            name: name || '',
            criteria: criteria || '',
            validationDate: validationDate || ''
        }];
    }
};

const confirmSave = async () => {
    try {
        loading.value = true;
        showSaveConfirmation.value = false;
        await handleSave();
        showAlert('Éxito', 'Los datos se han guardado correctamente', 'success');
    } catch (error) {
        showAlert('Error', error.message || 'Error al guardar los datos.', 'error');
    } finally {
        loading.value = false;
    }
};

const formatDeliverableCriteriaToString = (data) => {
    let result = '&Código|Nombre|Criterios|Fecha&';
    data.deliverableCriteria.forEach(criterias => {
        result += `${criterias.code}|${criterias.name}|${criterias.criteria}|${criterias.validationDate}&`;
    });
    return result;
};

const handleSave = async () => {
    try {
        const userId = AuthStore.userId;
        const token = AuthStore.token;
        const projectId = ProjectStore.projectId;

        const dataToSend = {
            ...formData,
            deliverableCriteria: formatDeliverableCriteriaToString(formData)
        };

        const exitId = await getExistingExit(ProcessStore.processId, token);
        let finalExitId = exitId;

        if (!exitId) {
            finalExitId = await createNewExit(ProcessStore.processId, AuthStore.name, token);
        }

        const response = await fetch(
            `http://localhost:8080/api/v1/parameters/saveParametersList?idExit=${finalExitId}`, 
            {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(dataToSend),
            }
        );

        if (!response.ok) {
            throw new Error('Error al guardad los datos');
        }

        const responseText = await response.text();

        // Si la respuesta esta vacia
        if (response.ok && (!responseText || responseText.trim() === '')) {
            return { success: true, message: 'Datos guardados correctamente' };
        }

        try {
            const responseData = JSON.parse(responseText);
            return responseData;
        } catch (e) {
            if (response.ok) {
                return { success: true, message: 'Datos guardados correctamente' };
            }
            throw new Error('Respuesta inválida del servidor');
        }

    } catch (error) {
        showAlert('Error en handleSave:',error);
        throw error;
    }
};

const showAlert = (title, message, type = 'info') => {
    alert.title = title;
    alert.message = message;
    alert.type = type;
    alert.show = true;
};

const handleAlertConfirm = () => {
    alert.show = false;
    if (alert.type === 'error' && alert.message.includes('Sesión no iniciada')) {
        navigateTo('/login');
    }
};

const addDeliverable = () => {
    if (!validateForm()) {
        errorMessage.value = 'Por favor, complete todos los campos obligatorios';
        return;
    }

    // Crear nuevo entregable con código automático
    formData.lastENT++;
    
    const newDeliverable = {
        code: `ENT-${String(formData.lastENT).padStart(3, '0')}`,
        name: currentDeliverable.name,
        criteria: currentDeliverable.criteria,
        validationDate: currentDeliverable.validationDate
    }

    // Agregar al array de entregables
    formData.deliverableCriteria.push({ ...newDeliverable });

    // Incrementar el contador de código
    console.log(formData.lastENT);

    // Limpiar el formulario
    resetForm();

    // Cerrar el popup
    closePopup();
};

const deleteDeliverable = (code) => {
  const index = formData.deliverableCriteria.findIndex(item => item.code === code);
  if (index !== -1) {
    formData.deliverableCriteria.splice(index, 1);
  }
};

// TODO: Entender como hacer que se pueda editar abriendo el popup
const editDeliverable = (item) => {
    openEditPopup(item);
    console.log('Editando entregable:', item);
};

const resetForm = () => {
    currentDeliverable.code = '';
    currentDeliverable.name = '';
    currentDeliverable.criteria = '';
    currentDeliverable.validationDate = '';
};

const filterPipeCharacter = (event, field) => {
    // Reemplaza cualquier carácter "|" con una cadena vacía
    const filteredValue = event.target.value.replace(/\|/g, '');
    // Actualiza el valor directamente en el objeto reactivo
    currentDeliverable[field] = filteredValue;
};

const hasPipe = (value) => {
    return value && value.includes('|');
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
    border-radius: 24px;
}

.title-content-text {
    max-width: 800px;
    margin: 0 auto;
    color: #000000;
    font-size: 1.5rem;
}

.text-container {
  margin: 20px 0;
  padding: 15px;
  background-color: #fff;
  border-radius: 16px;
}

.text-block {
  line-height: 1.6;
  color: #333;
}

.edit-button {
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  width: 30px;
  height: 30px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background-color 0.3s;
}

.edit-button:hover {
  background-color: #45a049;
}

.edit-icon {
  font-size: 16px;
  line-height: 1;
}


.form-container {
    max-width: 1200px;
    margin: 2rem auto;
    background: white;
    padding: 2rem;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.performanceReview-form {
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

select.form-input {
    appearance: none;
    background-image: url("data:image/svg+xml;charset=UTF-8,%3csvg xmlns='http://www.w3.org/2000/svg' width='24' height='24' viewBox='0 0 24 24' fill='none' stroke='currentColor' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3e%3cpolyline points='6 9 12 15 18 9'%3e%3c/polyline%3e%3c/svg%3e");
    background-repeat: no-repeat;
    background-position: right 0.5rem center;
    background-size: 1em;
    padding-right: 2.5rem;
}

select.form-input:focus {
    border-color: #00B8B0;
    outline: none;
}

select.form-input option {
    color: #000000;
    padding: 0.5rem;
}

.characteristics-row {
    margin-bottom: 1.5rem;
    background-color: #ffffff;
    padding: 1rem;
    border-radius: 4px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.characteristics-inputs {
    display: grid;
    grid-template-columns: 1fr 1fr 1fr;
    gap: 1rem;
    margin-bottom: 0.5rem;
}

.add-characteristics-container {
    display: flex;
    justify-content: center;
    margin-top: 1.5rem;
}

.add-characteristics-button {
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

.delete-button {
  background-color: #ff4444;
  color: white;
  border: none;
  border-radius: 4px;
  width: 30px;
  height: 30px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background-color 0.3s;
}

.delete-button:hover {
  background-color: #cc0000;
}

.delete-icon {
  font-size: 18px;
  line-height: 1;
}

.add-characteristics-button:hover {
    background-color: #009B94;
}

.remove-characteristics-button {
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

.remove-characteristics-button:hover {
    background-color: #c82333;
}

.characteristics-row {
    position: relative;
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

.action-column {
  width: 80px;
  text-align: center;
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
    color: #333;
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

.red-text {
  color: red;
}

.popup input::placeholder {
    color: black;
}

.popup-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
}

.popup {
    background: white;
    padding: 20px;
    border-radius: 12px;
    width: 800px;
    overflow-y: auto;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    color: black;
    position: relative;
}

.table-container {
    width: 100%;
    overflow-x: auto;
    margin: 1rem 0;
    border-radius: 16px;
}

.data-table {
    width: 100%;
    border-collapse: collapse;
    background-color: white;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    table-layout: fixed;
}

.data-table th,
.data-table td {
    padding: 12px;
    border: 1px solid #ddd;
    vertical-align: top;
}

.criteria-content {
  white-space: pre-wrap; /* Mantiene los saltos de línea y espacios */
  word-wrap: break-word; /* Rompe palabras largas */
  overflow-wrap: break-word;
  max-width: 100%;
  min-width: 0;
}

.data-table th {
    background-color: #f8f9fa;
    font-weight: 600;
    color: #333;
}

.data-table tr:hover {
    background-color: #f5f5f5;
}

.data-table td {
    color: #666;
}

@media (max-width: 768px) {
    .characteristics-inputs {
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

    .characteristics-row {
        margin-bottom: 1rem;
    }

    .characteristics-inputs {
        display: grid;
        grid-template-columns: repeat(3, 1fr) auto;
        gap: 1rem;
        align-items: flex-end;
        margin-bottom: 1rem;
    }

    .remove-characteristics-button {
        padding: 0.75rem 1rem;
        background-color: #dc3545;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        height: 42px;
        margin-bottom: 8px;
    }

    .data-table {
        font-size: 14px;
    }

    .id-column {
        width: 80px;
    }

    .date-column {
        width: 120px;
    }

    .data-table th,
    .data-table td {
        padding: 8px;
        font-size: 0.9rem;
    }

    .remove-characteristics-button:hover {
        background-color: #c82333;
    }

    .add-characteristics-container {
        margin-top: 1rem;
    }

    .action-column {
        width: 60px;
    }

    .add-characteristics-button {
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

    .add-characteristics-button:hover {
        background-color: #218838;
    }

    @media (max-width: 768px) {
        .characteristics-inputs {
            grid-template-columns: 1fr;
        }

        .remove-characteristics-button {
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
    display: block;
}

/* Estilos adicionales para el diálogo de guardar */
.save-confirmation .modal-content {
    background-color: #f8f9fa;
}

.save-confirmation .save-button {
    background-color: #28a745;
}

.save-confirmation .save-button:hover {
    background-color: #218838;
}

/* Nuevo estilo para el botón de cierre */
.close-button {
  position: absolute;
  top: 10px;
  right: 10px;
  width: 30px;
  height: 30px;
  border: none;
  background-color: transparent;
  font-size: 24px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
  transition: color 0.3s ease;
  padding: 0;
  line-height: 1;
}

.close-button:hover {
  color: #000;
}

.form-check-group {
    display: flex;
    gap: 50px; /*Espacio horizontal entre las opciones */
    /*justify-content: space-around;*/
    align-items: center;
    margin-left: 30px
}

.form-check-vertical {
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
}

/* Estilo para las etiquetas */
.custom-radio-label {
    position: relative;
    display: inline-block;
    cursor: pointer;
    font-size: 14px;
    color: #333; /* Color del texto */
}

/* Ocultar el input original */
.custom-radio-input {
    display: none;
}

/* Estilo de las casillas cuadradas */
.custom-radio-box {
    display: inline-block;
    width: 24px;
    height: 24px;
    margin-top: 8px;
    border: 2px solid #ccc;
    border-radius: 4px; /* Borde redondeado */
    background-color: #fff; /* Fondo blanco */
    transition: all 0.3s ease;
}

/* Estilo al seleccionar */
.custom-radio-input:checked + .custom-radio-box {
    background-color: #00B8B0; /* Fondo verde minimalista */
    border-color: #00B8B0; /* Borde del mismo color */
}

.custom-radio-box:hover {
    border-color: #666; /* Cambia el borde al pasar el cursor */
}

.validation-form {
    display: flex;
    flex-direction: column;
    gap: 2rem;
}

/* Definir anchos específicos para cada columna */
.id-column {
  width: 100px;
}

.name-column {
  width: 20%;
}

.criteria-column {
  width: 40%;
}

.date-column {
  width: 150px;
}

</style>