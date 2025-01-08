<template>
    <div class="page-container">
        <main class="main-content">
            <div class="title-content">
                <h1 class="title-content-text">Revisión de Desempeño del Trabajo</h1>
            </div>

            <div class="form-container">
                <form @submit.prevent="handleSubmit" class="performanceReview-form">
                    <!-- Información básica -->
                    <section class="form-section">
                        <h2 class="section-title">Información Básica</h2>
                        <div class="form-group">
                            <label for="proyectName">Nombre del Proyecto <span class="red-text">*</span></label>
                            <input id="proyectName" v-model="formData.proyectName" type="text"
                                :class="['form-input', { 'invalid-input': isFieldInvalid('proyectName') }]" readonly
                                required />
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="idProject">ID del Proyecto <span class="red-text">*</span></label>
                                <input id="idProject" v-model="formData.idProject"
                                    class="form-input" readonly required></input>
                            </div>

                            <div class="form-group">
                                <label for="elaborationDate">Fecha de Elaboración <span class="red-text">*</span></label>
                                <input id="elaborationDate" v-model="formData.elaborationDate" type="date"
                                    class="form-input" readonly required></input>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="projectLeader">Líder del Proyecto <span class="red-text">*</span></label>
                            <input id="projectLeader" v-model="formData.projectLeader" type="text"
                                class="form-input" readonly required />
                        </div>
                    </section>

                    <!-- Resumen del Alcance -->
                    <section class="form-section">
                        <h2 class="section-title">Resumen del Alcance <span class="red-text">*</span></h2>
                        <div class="form-group">
                            <label for="scopeSummary">En esta sección se describe brevemente el propósito y los objetivos principales del proyecto, con un enfoque en el alcance aprobado. Se debe incluir una descripción de los entregables planificados, las actividades clave que se deben realizar para lograrlos y cualquier criterio de éxito acordado. Este resumen ayuda a contextualizar el progreso evaluado y a recordar los límites del alcance del proyecto.</label>
                            <textarea id="scopeSummary" v-model="formData.scopeSummary"
                                class="form-input form-textarea" required></textarea>
                        </div>
                    </section>

                    <!-- Progreso Actual -->
                    <section class="form-section">
                        <h2 class="section-title">Progreso actual <span class="red-text">*</span></h2>
                        <div class="form-group">
                            <label for="progress">Esta sección detalla los avances logrados en el proyecto en términos de tareas completadas, entregables producidos y el porcentaje de avance global. También se deben incluir métricas clave como el índice de desempeño del cronograma (SPI) y cualquier desviación respecto a lo planificado.</label>
                            <textarea id="progress" v-model="formData.progress"
                                class="form-input form-textarea" required></textarea>
                        </div>
                    </section>

                    <!-- Desviaciones Detectadas -->
                    <section class="form-section">
                        <h2 class="section-title">Desviaciones Detectadas <span class="red-text">*</span></h2>
                        <div class="form-group">
                            <label for="deviationsDetected">En esta sección se identifican y describen las desviaciones del plan de alcance, junto con las actividades o entregables afectados. Es importante detallar si estas desviaciones fueron causadas por factores internos (como problemas de recursos) o externos (como cambios en los requisitos).</label>
                            <textarea id="deviationsDetected" v-model="formData.deviationsDetected"
                                class="form-input form-textarea" required></textarea>
                        </div>
                    </section>

                    <!-- Análisis de causas -->
                    <section class="form-section">
                        <h2 class="section-title">Análisis de causas <span class="red-text">*</span></h2>
                        <div class="form-group">
                            <label for="analysisOfCauses">En esta sección se analizan las causas raíz de las desviaciones detectadas. Esto ayuda a comprender por qué ocurrieron y a identificar patrones que podrían prevenirse en el futuro.</label>
                            <textarea id="analysisOfCauses" v-model="formData.analysisOfCauses"
                                class="form-input form-textarea" required></textarea>
                        </div>
                    </section>

                    <!-- Acciones correctivas -->
                    <section class="form-section">
                        <h2 class="section-title">Acciones correctivas <span class="red-text">*</span></h2>
                        <div class="form-group">
                            <label for="correctiveActions">Aquí se describen las acciones tomadas o planificadas para resolver los problemas detectados y asegurar el cumplimiento del alcance. Es fundamental detallar quién será responsable de implementar estas medidas y los plazos esperados para su ejecución.</label>
                            <textarea id="correctiveActions" v-model="formData.correctiveActions"
                                class="form-input form-textarea" required></textarea>
                        </div>
                    </section>

                    <!-- Próximos pasos -->
                    <section class="form-section">
                        <h2 class="section-title">Próximos pasos <span class="red-text">*</span></h2>
                        <div class="form-group">
                            <label for="nextSteps">En esta sección se enumeran las actividades pendientes más importantes y los planes inmediatos para su ejecución. También se puede incluir una breve mención de los riesgos asociados y las medidas preventivas previstas.</label>
                            <textarea id="nextSteps" v-model="formData.nextSteps"
                                class="form-input form-textarea" required></textarea>
                        </div>
                    </section>

                    <!-- Botones de acción -->
                    <section class="form-actions">
                        <button type="button" class="cancel-button" @click="showCancelConfirmation = true">
                            Cancelar
                        </button>
                        <button type="button" class="save-button" :disabled="loading"
                            @click="showSaveConfirmation = true">
                            {{ loading ? 'Guardando...' : 'Guardar' }}
                        </button>
                        <button type="submit" class="submit-button" :disabled="loading">
                            {{ loading ? 'Creando...' : 'Crear Documento' }}
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
                <AlertPopup :show="alert.show" :title="alert.title" :message="alert.message" :type="alert.type"
                    @confirm="handleAlertConfirm" />
                <div v-if="pdfUrl" class="pdf-container">
                    <iframe :src="pdfUrl" class="pdf-iframe"></iframe>
                </div>
                <div class="pdf-footer">
                <button @click="navigateTo('/principalView')" class="pdf-button return-button">
                    Volver al Dashboard
                </button>
            </div>
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
const { fetch } = useFetchWithAuth();

// Reactive state
const loading = ref(false);
const errorMessage = ref('');
const showCancelConfirmation = ref(false);
const showSaveConfirmation = ref(false);
const pdfUrl = ref(null);

// Initial form data
const formData = reactive({
    proyectName: '',
    idProject: '',
    projectLeader: '',
    elaborationDate: new Date().toISOString().split('T')[0],
    scopeSummary: '',
    progress: '',
    deviationsDetected: '',
    analysisOfCauses: '',
    correctiveActions: '',
    nextSteps: ''
});

const alert = reactive({
    show: false,
    title: '',
    message: '',
    type: 'info',
});

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
            nameExit: 'Informes de desempeño del trabajo',
            state: 'Activo',
            dateCreation: new Date().toISOString().split('T')[0],
            dateValidation: new Date().toISOString().split('T')[0],
            priority: 'Alta',
            responsible: nameUser,
            description: 'Informes de desempeño del trabajo'
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
        `http://localhost:8080/api/v1/exit/getByIdProcessAndNameExit?idProcess=${processId}&nameExit=Informes de desempeño del trabajo`,
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
    return true;
};

const isFieldInvalid = (fieldName) => {
    return false; 
};

const handleCancel = () => {
    showCancelConfirmation.value = false;
    navigateTo('/ScopeManagementView');
};

onMounted(async () => {
    try {
        loading.value = true;
        const projectId = ProjectStore.projectId;
        const userId = AuthStore.userId;
        const token = AuthStore.token;

        const projectResponse = await fetch(`http://localhost:8080/api/v1/proyect/getById/${projectId}`, {
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        })

        const userResponse = await fetch(`http://localhost:8080/api/v1/user/getById/${userId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            }
        });

        if (projectResponse.ok && userResponse.ok) {
            const data = await projectResponse.json();
            const userDataResponse = await userResponse.json();
            formData.proyectName = data.nameProyect;
            formData.idProject = data.idProyecto;
            formData.projectLeader = `${userDataResponse.names} ${userDataResponse.secondNames}`;
            formData.elaborationDate = data.startDate;
        }

        const exitId = await getExistingExit(ProcessStore.processId, token);

        if (exitId) {
            const parametersResponse = await fetch(
                `http://localhost:8080/api/v1/exit/${exitId}/parameters`,
                {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                }
            );

            if (parametersResponse.ok) {
                const parameters = await parametersResponse.json();

                parameters.forEach(param => {
                    console.log(`Processing parameter: ${param.nameParameter}`, param.content);
                    switch (param.nameParameter) {
                        case 'scopeSummary':
                            formData.scopeSummary = param.content;
                            console.log(`Processing parameter: ${param.nameParameter}`, param.content);
                            break;
                        case 'progress':
                            formData.progress = param.content;
                            console.log(`Processing parameter: ${param.nameParameter}`, param.content);
                            break;
                        case 'deviationsDetected':
                            formData.deviationsDetected = param.content;
                            console.log(`Processing parameter: ${param.nameParameter}`, param.content);
                            break;
                        case 'analysisOfCauses':
                            formData.analysisOfCauses = param.content;
                            console.log(`Processing parameter: ${param.nameParameter}`, param.content);
                            break;
                        case 'correctiveActions':
                            formData.correctiveActions = param.content;
                            console.log(`Processing parameter: ${param.nameParameter}`, param.content);
                            break;
                        case 'nextSteps':
                            formData.nextSteps = param.content;
                            console.log(`Processing parameter: ${param.nameParameter}`, param.content);
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

const handleSubmit = async () => {
    if (!validateForm()) return;

    try {
        loading.value = true;
        const { userId, token, name: nameUser } = AuthStore;
        const processId = ProcessStore.processId;

        if (!userId || !token) {
            showAlert('Error de Sesión', '¡Sesión no iniciada!, redirigiendo a login...', 'error');
            router.push('/login');
            return;
        }

        // Get or create exit
        let exitId = await getExistingExit(processId, token);
        if (!exitId) {
            exitId =  await createNewExit(processId, nameUser, token);
        }

        // Convertir los arrays al formato requerido
        const formattedData = {
            ...formData,
        }

        // Ahora solo necesitamos hacer una única llamada al endpoint actualizado
        const response = await fetch(`http://localhost:8080/api/documents/work-performance-review/generate?idExit=${exitId}`, 
            {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formattedData),
            }
        );

        if (!response.ok) {
            throw new Error(await response.text());
        }

        // Procesar la respuesta del PDF
        const blob = await response.blob();
        pdfUrl.value = URL.createObjectURL(blob);

    } catch (error) {
        showAlert('Error', error.message, 'error');
    } finally {
        loading.value = false;
    }
};

const confirmSave = async () => {
    try {
        loading.value = true;
        showSaveConfirmation.value = false;
        await handleSave();
        showAlert('Éxito', 'Los datos se han guardado correctamente', 'success');
    } catch (error) {
        showAlert('Error', 'Error al guardar los datos.', 'error');
    } finally {
        loading.value = false;
    }
};

const handleSave = async () => {
    try {
        const userId = AuthStore.userId;
        const token = AuthStore.token;
        const projectId = ProjectStore.projectId;

        const dataToSend = {
            ...formData,
        }

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

const goToDashboard = () => {
    router.push('/dashboard');
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

.pdf-footer {
    display: flex;
    justify-content: flex-end;
    gap: 1rem;
    padding-top: 1rem;
    border-top: 1px solid #eee;
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

    .remove-characteristics-button:hover {
        background-color: #c82333;
    }

    .add-characteristics-container {
        margin-top: 1rem;
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

</style>
