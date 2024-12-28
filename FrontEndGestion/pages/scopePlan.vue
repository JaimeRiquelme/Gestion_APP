<template>
    <div class="page-container">
        <main class="main-content">
            <div class="title-content">
                <h1 class="title-content-text">Plan de Gestión del Alcance</h1>
            </div>

            <div class="form-container">
                <form @submit.prevent="handleSubmit" class="constitution-form">
                    <!-- Información básica -->
                    <section class="form-section">
                        <h2 class="section-title">Información Básica</h2>
                        <div class="form-group">
                            <label for="proyectName">Nombre del Proyecto <span class="red-text">*</span></label>
                            <input id="proyectName" v-model="formData.proyectName" type="text" class="form-input"
                                readonly required />
                        </div>

                        <div class="form-group">
                            <label for="idProyect">ID del Proyecto <span class="red-text">*</span></label>
                            <input id="idProyect" v-model="formData.idProyect" type="text" class="form-input" readonly
                                required />
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="proyectLeader">Líder del Proyecto <span class="red-text">*</span></label>
                                <input id="proyectLeader" v-model="formData.proyectLeader" type="text"
                                    class="form-input" required />
                            </div>

                            <div class="form-group">
                                <label for="productLeader">Líder de Producto <span class="red-text">*</span></label>
                                <input id="productLeader" v-model="formData.productLeader" type="text"
                                    class="form-input" readonly required />
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="elaborationDate">Fecha de Elaboración <span class="red-text">*</span></label>
                            <input id="elaborationDate" v-model="formData.elaborationDate" type="date"
                                class="form-input" readonly required />
                        </div>
                    </section>

                    <!-- Enfoque del Alcance -->
                    <section class="form-section">
                        <h2 class="section-title">Enfoque del Alcance</h2>
                        <div class="form-group">
                            <label for="scopeApproach">Descripción <span class="red-text">*</span></label>
                            <textarea id="scopeApproach" v-model="formData.scopeApproach"
                                class="form-input form-textarea" required></textarea>
                        </div>

                        <div class="form-group">
                            <label for="scopePlanObjectives">Objetivos del Plan de Gestión del Alcance <span class="red-text">*</span></label>
                            <textarea id="scopePlanObjectives" v-model="formData.scopePlanObjectives"
                                class="form-input form-textarea" required></textarea>
                        </div>
                    </section>

                    <!-- Definición del Alcance -->
                    <section class="form-section">
                        <h2 class="section-title">Definición del Alcance</h2>
                        <div class="form-group">
                            <label for="scopeDefinition">Descripción <span class="red-text">*</span></label>
                            <textarea id="scopeDefinition" v-model="formData.scopeDefinition"
                                class="form-input form-textarea" required></textarea>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="scopeExclusions">Exclusiones del Alcance <span class="red-text">*</span></label>
                                <textarea id="scopeExclusions" v-model="formData.scopeExclusions"
                                class="form-input form-textarea" required></textarea>
                            </div>

                            <div class="form-group">
                                <label for="scopeLimitations">Limitaciones del Alcance <span class="red-text">*</span></label>
                                <textarea id="scopeLimitations" v-model="formData.scopeLimitations"
                                    class="form-input form-textarea" required></textarea>
                            </div>
                        </div>
                    </section>

                    <!-- Enunciado del Alcance -->
                    <section class="form-section">
                        <h2 class="section-title">Enunciado del Alcance</h2>
                        <div class="form-group">
                            <label for="scopeStatement">Descripción <span class="red-text">*</span></label>
                            <textarea id="scopeStatement" v-model="formData.scopeStatement"
                                class="form-input form-textarea" required></textarea>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="scopeOutputs">Entregables del Alcance <span class="red-text">*</span></label>
                                <textarea id="scopeOutputs" v-model="formData.scopeOutputs"
                                class="form-input form-textarea" required></textarea>
                            </div>

                            <div class="form-group">
                                <label for="projectAssumptions">Suposiciones del Alcance <span class="red-text">*</span></label>
                                <textarea id="projectAssumptions" v-model="formData.projectAssumptions"
                                    class="form-input form-textarea" required></textarea>
                            </div>
                        </div>
                    </section>

                    <!-- Recopilación de Requisitos -->
                    <section class="form-section">
                        <h2 class="section-title">Recopilación de Requisitos</h2>
                        <div class="form-group">
                            <label for="requirementGathering">Descripción <span class="red-text">*</span></label>
                            <textarea id="requirementGathering" v-model="formData.requirementGathering"
                                class="form-input form-textarea" required></textarea>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="requirementsGatheringStrategy">Estrategias para la Recopilación de Requisitos <span class="red-text">*</span></label>
                                <textarea id="requirementsGatheringStrategy" v-model="formData.requirementsGatheringStrategy"
                                    class="form-input form-textarea" required></textarea>
                            </div>

                            <div class="form-group">
                                <label for="requirementsAnalysis">Análisis de Requisitos <span class="red-text">*</span></label>
                                <textarea id="requirementsAnalysis" v-model="formData.requirementsAnalysis"
                                    class="form-input form-textarea" required></textarea>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="requirementsDocumentation">Documentación de Requisitos <span class="red-text">*</span></label>
                            <textarea id="requirementsDocumentation" v-model="formData.requirementsDocumentation"
                                class="form-input form-textarea" required></textarea>
                        </div>
                    </section>

                    <!-- Verificación y Control del Alcance -->
                    <section class="form-section">
                        <h2 class="section-title">Verificación y Control del Alcance</h2>
                        <div class="form-group">
                            <label for="verificationAndControlOfScope">Descripción <span class="red-text">*</span></label>
                            <textarea id="verificationAndControlOfScope" v-model="formData.verificationAndControlOfScope"
                                class="form-input form-textarea" required></textarea>
                        </div>

                        <div class="form-group">
                            <label for="scopeApproval">Aprobación del Alcance <span class="red-text">*</span></label>
                            <textarea id="scopeApproval" v-model="formData.scopeApproval" class="form-input form-textarea"
                                required></textarea>
                        </div>

                        <div class="form-group">
                            <label for="stakeholdersApproval">Aceptación Formal por parte de los Interesados <span class="red-text">*</span></label>
                            <textarea id="stakeholdersApproval" v-model="formData.stakeholdersApproval"
                                class="form-input form-textarea" required></textarea>
                        </div>

                        <div class="form-group">
                            <label for="changeControlSystem">Sistema de Control de Cambios <span class="red-text">*</span></label>
                            <textarea id="changeControlSystem" v-model="formData.changeControlSystem"
                                class="form-input form-textarea" required></textarea>
                        </div>

                        <div class="form-group">
                            <label for="edtApproval">Aprobación Formal del EDT <span class="red-text">*</span></label>
                            <textarea id="edtApproval" v-model="formData.edtApproval"
                                class="form-input form-textarea" required></textarea>
                        </div>
                    </section>

                    <section class="form-actions">
                        <div class="form-actions">
                            <button type="button" class="cancel-button" @click="showCancelConfirmation = true">
                                Cancelar
                            </button>
                            <button type="button" class="save-button" :disabled="loading" @click="handleSave">
                                {{ loading ? 'Guardando...' : 'Guardar' }}
                            </button>
                            <button type="submit" class="submit-button" :disabled="loading">
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
                    </section>
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
    idProyect: '',
    company: '',
    proyectLeader: '',
    productLeader: '',
    elaborationDate: new Date().toISOString().split('T')[0],
    scopeApproach: '',
    scopePlanObjectives: '',
    scopeDefinition: '',
    scopeExclusions: '',
    scopeLimitations: '',
    scopeStatement: '',
    scopeOutputs: '',
    projectAssumptions: '',
    requirementGathering: '',
    requirementsGatheringStrategy: '',
    requirementsAnalysis: '',
    requirementsDocumentation: '',
    verificationAndControlOfScope: '',
    scopeApproval: '',
    stakeholdersApproval: '',
    changeControlSystem: '',
    edtApproval: ''
});


const showCancelConfirmation = ref(false);

const handleCancel = () => {
    showCancelConfirmation.value = false;
    navigateTo('/dashboard');
};

const fetchProjectData = async () => {
    try {
        loading.value = true;
        errorMessage.value = '';

        const userIdCookie = useCookie('userId');
        const tokenCookie = useCookie('authToken');
        const projectIdCookie = useCookie('projectId');

        // TODO: DESCOMENTAR
        /*
        if (!userIdCookie.value || !tokenCookie.value || !projectIdCookie.value) {
            alert('ALERTA: ¡Sesión no iniciada o proyecto no seleccionado!, redirigiendo a login...')
            await navigateTo('/login')
            return;
        }
        */

        // Obtener la información del proyecto
        const respondeProyect = await fetch(`http://localhost:8080/api/v1/proyect/getById/${projectIdCookie.value}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${tokenCookie.value}`,
                'Content-Type': 'application/json',
            }
        });

        //imprimo por consola la informacion del proyecto
        console.log(respondeProyect);

        // Obtener la información del usuario
        const respondeUser = await fetch(`http://localhost:8080/api/v1/user/getById/${userIdCookie.value}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${tokenCookie.value}`,
                'Content-Type': 'application/json',
            }
        });

        //imprimo por consola la informacion del usuario
        console.log(respondeUser);

        if (!respondeProyect.ok || !respondeUser.ok) {
            throw new Error('Error al obtener la información necesaria');
        }

        const projectDataResponse = await respondeProyect.json();
        const userDataResponse = await respondeUser.json();

        // Actualizar el formData
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

        const respondeProyect = await fetch('http://localhost:8080/api/documents/scope-management-plan/create', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${tokenCookie.value}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                ...formData,
                proyectStakeholders: formattedStakeholders,
                rolesAndResponsabilities: formattedRoles,
                idUsuario: parseInt(userIdCookie.value),
            }),
        });

        if (!respondeProyect.ok) {
            const errorData = await respondeProyect.json();
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

const handleSave = () => {
    // Crear el objeto con los datos formateados
    const formattedData = {
        ...formData,
    };

    // Mostrar los datos en la consola de manera formateada
    console.log('Datos del formulario (raw):', formData);
    console.log('Datos formateados para envío:', formattedData);
    
    // Mostrar el JSON formateado
    console.log('JSON completo:', JSON.stringify(formattedData, null, 2));
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

.stakeholder-row {
    margin-bottom: 1.5rem;
    background-color: #ffffff;
    padding: 1rem;
    border-radius: 4px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.stakeholder-inputs {
    display: grid;
    grid-template-columns: 1fr 1fr 1fr;
    gap: 1rem;
    margin-bottom: 0.5rem;
}

.add-stakeholder-container {
    display: flex;
    justify-content: center;
    margin-top: 1.5rem;
}

.add-stakeholder-button {
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

.add-stakeholder-button:hover {
    background-color: #009B94;
}

.red-text {
  color: red;
}

.remove-stakeholder-button {
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

.remove-stakeholder-button:hover {
    background-color: #c82333;
}

.stakeholder-row {
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
</style>