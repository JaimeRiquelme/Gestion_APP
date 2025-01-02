<template>
    <div class="page-container">
        <main class="main-content">
            <div class="title-content">
                <h1 class="title-content-text">Revisión de Desempeño del Trabajo</h1>
            </div>

            <div class="form-container">
                <form @submit.prevent="handleSubmit" class="performanceReview-form">
                    <!-- Información del Empleado -->
                    <section class="form-section">
                        <h2 class="section-title">Información del Empleado</h2>
                        <div class="form-group">
                            <label for="employeeName">Nombre del Empleado <span class="red-text">*</span></label>
                            <input id="employeeName" v-model="formData.employeeName" type="text"
                                class="form-input" required></input>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="employeeDepartment">Departamento <span class="red-text">*</span></label>
                                <input id="employeeDepartment" v-model="formData.employeeDepartment"
                                    class="form-input" required></input>
                            </div>

                            <div class="form-group">
                                <label for="employeeId">ID de Empleado <span class="red-text">*</span></label>
                                <input id="employeeId" v-model="formData.employeeId"
                                    class="form-input" required></input>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="reviewerName">Nombre Revisor <span class="red-text">*</span></label>
                            <input id="reviewerName" v-model="formData.reviewerName" type="text"
                                class="form-input" required></input>
                        </div>

                        <div class="form-group">
                            <label for="employeePosition">Posición Empleado <span class="red-text">*</span></label>
                            <input id="employeePosition" v-model="formData.employeePosition" type="text"
                                class="form-input" required></input>
                        </div>

                        <div class="form-group">
                            <label for="reviewerTitle">Título del Revisor <span class="red-text">*</span></label>
                            <input id="reviewerTitle" v-model="formData.reviewerTitle" type="text"
                                class="form-input" required></input>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="lastReviewDate">Fecha de Última Revisión <span class="red-text">*</span></label>
                                <input id="lastReviewDate" v-model="formData.lastReviewDate" type="date"
                                    class="form-input" required></input>
                            </div>

                            <div class="form-group">
                                <label for="currentDate">Fecha Actual <span class="red-text">*</span></label>
                                <input id="currentDate" v-model="formData.currentDate" type="date"
                                    class="form-input" required></input>
                            </div>
                        </div>

                    </section>

                    <!-- Evaluación de de Características del Empleado -->
                    <section class="form-section">
                        <h2 class="section-title">Evaluación de de Características del Empleado</h2>

                        <div v-for="(characteristics, index) in formData.employeeCharacteristicsEvaluations" :key="index"
                            class="characteristics-row">
                            <div
                                :class="['characteristics-inputs', { 'invalid-section': isFieldInvalid(`characteristics-${index}`) }]">

                                <div class="form-group">
                                    <label :for="'characteristicName' + index">Característica <span class="red-text">*</span></label>
                                    <input :id="'characteristicName' + index" v-model="characteristics.name"
                                        type="text" class="form-input" placeholder="Ingrese el nombre"
                                        @input="validateNoCommas($event, 'characteristics', index, 'position')" required />
                                    <span v-if="hasComma(characteristics.position)" class="invalid-feedback">No se permiten
                                        comas en este campo</span>
                                </div>

                                <div class="form-group">
                                    <label :for="'characteristicType' + index">Calificación <span class="red-text">*</span></label>
                                    <div class="form-check-group">
                                        <div 
                                        v-for="option in ['Insuficiente', 'Satisfactoria', 'Buena', 'Excelente']" 
                                        :key="option" 
                                        class="form-check-vertical"
                                        >
                                        <label 
                                            :for="'characteristicType-' + index + '-' + option" 
                                            class="custom-radio-label"
                                        >
                                            {{ option }}
                                            <input 
                                            type="radio" 
                                            :id="'characteristicType-' + index + '-' + option" 
                                            :value="option" 
                                            v-model="characteristics.qualification" 
                                            class="custom-radio-input" 
                                            required
                                            />
                                            <span class="custom-radio-box"></span>
                                        </label>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <button v-if="formData.employeeCharacteristicsEvaluations.length > 1" type="button"
                                class="remove-characteristics-button" @click="removeEmployeeCharacteristics(index)">
                                ×
                            </button>
                        </div>

                        <div class="add-characteristics-container">
                            <button type="button" class="add-characteristics-button" @click="addEmployeeCharacteristics">
                                +
                            </button>
                        </div>
                    </section>


                    <!-- Metas -->
                    <section class="form-section">
                        <h2 class="section-title">Metas</h2>
                        <div class="form-group">
                            <label for="currentGoalsReview">¿Fueron las metas alcanzadas en la actual revisión? <span class="red-text">*</span></label>
                            <textarea id="currentGoalsReview" v-model="formData.currentGoalsReview"
                                class="form-input form-textarea" required></textarea>
                        </div>

                        <div class="form-group">
                            <label for="futureGoals">Metas para la siguiente revisión: <span class="red-text">*</span></label>
                            <textarea id="futureGoals" v-model="formData.futureGoals"
                                class="form-input form-textarea" required></textarea>
                        </div>
                    </section>

                    <!-- Comentarios y Aprobación -->
                    <section class="form-section">
                        <h2 class="section-title">Comentarios y Aprobación</h2>
                        <div class="form-group">
                            <label for="comments">Comentarios: <span class="red-text">*</span></label>
                            <textarea id="comments" v-model="formData.comments"
                                class="form-input form-textarea" required></textarea>
                        </div>
                    </section>

                    <section class="form-actions">
                        <div class="form-actions">
                            <button type="button" class="cancel-button" @click="showCancelConfirmation = true">
                                Cancelar
                            </button>
                            <button type="button" class="save-button" :disabled="loading"
                                @click="showSaveConfirmation = true">
                                {{ loading ? 'Guardando...' : 'Guardar' }}
                            </button>
                            <button type="submit" class="submit-button" :disabled="loading" @click="handleSubmit">
                                {{ loading ? 'Creando...' : 'Crear Documento' }}
                            </button>
                        </div>

                        <!-- Diálogo de confirmación para cancelar -->
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

                        <!-- Diálogo de confirmación para guardar -->
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
                    </section>
                </form>

            </div>
        </main>
    </div>

    <!-- Agregar el componente AlertPopup -->
    <AlertPopup
        :show="alert.show"
        :title="alert.title"
        :message="alert.message"
        :type="alert.type"
        @confirm="handleAlertConfirm"
    />
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

// Initial form data
const formData = reactive({
    employeeName: '',
    employeeDepartment: '',
    employeeId: '',
    reviewerName: '',
    employeePosition: '',
    reviewerTitle: '',
    lastReviewDate: new Date().toISOString().split('T')[0],
    currentDate: '', // TODO: Verificar como hacer esto, problamente solucionado en el backend.
    employeeCharacteristicsEvaluations: [{ // TODO: Rellenar con el backend
        number: '',
        name: '',
        qualification: ''
    }],
    currentGoalsReview: '',
    futureGoals: '',
    comments: ''
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
    /*
    invalidFields.value.clear();

    // Validar campos básicos
    Object.entries(formData).forEach(([key, value]) => {
        if (typeof value === 'string' && !value.trim()) {
            invalidFields.value.add(key);
        }
    });

    // Validar caracteristicas
    formData.employeeCharacteristicsEvaluations.forEach((characteristic, index) => {
        if (!characteristic.name || !characteristic.qualification) {
            invalidFields.value.add(`characteristic-${index}`);
        }
    });

    // Agregar validación de comas
    formData.employeeCharacteristicsEvaluations.forEach((characteristics, index) => {
        if (hasComma(characteristics.position) || hasComma(characteristics.name)) {
            invalidFields.value.add(`characteristic-${index}`);
        }
    });

    return invalidFields.value.size === 0;
    */
    return true;
};

const handleCancel = () => {
    showCancelConfirmation.value = false;
    navigateTo('/principalView');
};

const addEmployeeCharacteristics = () => {
    formData.employeeCharacteristicsEvaluations.push({
        characteristic: '',
        qualification: ''
    });
};

const removeEmployeeCharacteristics = (index) => {
    formData.employeeCharacteristicsEvaluations.splice(index, 1);
};

onMounted(() => {
    // fetchProjectData();
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
            employeeCharacteristicsEvaluations: formatCharacteristicToString(formData),
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
        window.open(URL.createObjectURL(blob));
        router.push('/ScopeManagementView');

    } catch (error) {
        showAlert('Error', error.message, 'error');
    } finally {
        loading.value = false;
    }
};

// Función para formatear las características según la documentación
const formatCharacteristicToString = (data) => {
    let result = '&Número,Nombre,Calificación&';
    let index = 1;
    data.employeeCharacteristicsEvaluations.forEach(characteristic => {
        result += `${index},${characteristic.name},${characteristic.qualification}&`;
        index++;
    });
    return result;
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
    const userId = AuthStore.userId;
    const token = AuthStore.token;
    const projectId = ProjectStore.projectId;

    const dataToSend = {
        ...formData,
        employeeCharacteristicsEvaluations: formatCharacteristicToString(formData)
    }

    console.log("JSON para Postman:", JSON.stringify(dataToSend, null, 2));

    /*
    try {
        const userId = AuthStore.userId;
        const token = AuthStore.token;
        const projectId = ProjectStore.projectId;

        const dataToSend = {
            ...formData,
            employeeCharacteristicsEvaluations: formatCharacteristicToString(formData)
        }

        const saveData = await fetch(`http://localhost:8080/api/v1/parameters/saveParametersList?idExit=${ProjectStore.projectId}`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                ...formData,
                employeeCharacteristicsEvaluations: formattedCharacteristics,
            }),
        }); 

        if (!saveData.ok) {
            const errorData = await saveData.json();
            throw new Error(errorData.message || 'Error al guardar los datos');
        }

        return await saveData.json();
    } catch (error) {
        showAlert('Error', 'Error al guardar los datos. Por favor, intenta nuevamente.', 'error');
        throw error;
    }
    */
};

const isFieldInvalid = (fieldName) => {
    return invalidFields.value.has(fieldName);
};

const hasComma = (value) => {
    return value && value.includes(',');
};

const validateNoCommas = (event, type, index, field) => {
    const value = event.target.value;
    if (value.includes(',')) {
        if (type === 'characteristic') {
            formData.employeeCharacteristicsEvaluations[index][field] = value.replace(/,/g, '');
        }
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