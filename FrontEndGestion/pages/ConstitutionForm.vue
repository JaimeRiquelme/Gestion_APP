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
                            <input id="proyectName" v-model="formData.proyectName" type="text"
                                :class="['form-input', { 'invalid-input': isFieldInvalid('proyectName') }]"
                                @focus="invalidFields.delete('proyectName')" readonly required />
                        </div>

                        <div class="form-group">
                            <label for="idProyect">ID del Proyecto *</label>
                            <input id="idProyect" v-model="formData.idProyect" type="text" class="form-input" readonly
                                required />
                        </div>

                        <div class="form-group">
                            <label for="company">Empresa Encargada *</label>
                            <input id="company" v-model="formData.company" type="text" class="form-input" readonly
                                required />
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="proyectPromotor">Promotor del Proyecto *</label>
                                <input id="proyectPromotor" v-model="formData.proyectPromotor" type="text"
                                    class="form-input" required />
                            </div>

                            <div class="form-group">
                                <label for="proyectLeader">Líder del Proyecto *</label>
                                <input id="proyectLeader" v-model="formData.proyectLeader" type="text"
                                    class="form-input" readonly required />
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="elaborationDate">Fecha de Elaboración *</label>
                            <input id="elaborationDate" v-model="formData.elaborationDate" type="date"
                                class="form-input" readonly required />
                        </div>
                    </section>

                    <!-- Propósito y Descripción -->
                    <section class="form-section">
                        <h2 class="section-title">Propósito y Descripción</h2>
                        <div class="form-group">
                            <label for="proyectPurpouse">Propósito del Proyecto *</label>
                            <textarea id="proyectPurpouse" v-model="formData.proyectPurpouse"
                                class="form-input form-textarea" required></textarea>
                        </div>

                        <div class="form-group">
                            <label for="proyectDescription">Descripción del Proyecto *</label>
                            <textarea id="proyectDescription" v-model="formData.proyectDescription"
                                class="form-input form-textarea" required></textarea>
                        </div>
                    </section>

                    <!-- Stakeholders -->
                    <section class="form-section">
                        <h2 class="section-title">Stakeholders</h2>

                        <div v-for="(stakeholder, index) in formData.proyectStakeholders" :key="index"
                            class="stakeholder-row">
                            <div
                                :class="['stakeholder-inputs', { 'invalid-section': isFieldInvalid(`stakeholder-${index}`) }]">
                                <div class="form-group">
                                    <label :for="'stakeholderType' + index">Tipo *</label>
                                    <select :id="'stakeholderType' + index" v-model="stakeholder.type"
                                        class="form-input" required>
                                        <option value="" disabled selected>Seleccione un tipo</option>
                                        <option value="Interno">Interno</option>
                                        <option value="Externo">Externo</option>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label :for="'stakeholderPosition' + index">Cargo *</label>
                                    <input :id="'stakeholderPosition' + index" v-model="stakeholder.position"
                                        type="text" class="form-input" placeholder="Ingrese el cargo"
                                        @input="validateNoCommas($event, 'stakeholder', index, 'position')" required />
                                    <span v-if="hasComma(stakeholder.position)" class="invalid-feedback">No se permiten
                                        comas en este campo</span>
                                </div>

                                <div class="form-group">
                                    <label :for="'stakeholderName' + index">Nombre *</label>
                                    <input :id="'stakeholderName' + index" v-model="stakeholder.name" type="text"
                                        class="form-input" placeholder="Ingrese el nombre"
                                        @input="validateNoCommas($event, 'stakeholder', index, 'name')" required />
                                    <span v-if="hasComma(stakeholder.name)" class="invalid-feedback">No se permiten
                                        comas en este campo</span>
                                </div>
                            </div>

                            <button v-if="formData.proyectStakeholders.length > 1" type="button"
                                class="remove-stakeholder-button" @click="removeStakeholder(index)">
                                ×
                            </button>
                        </div>

                        <div class="add-stakeholder-container">
                            <button type="button" class="add-stakeholder-button" @click="addStakeholder">
                                +
                            </button>
                        </div>
                    </section>


                    <!-- Objetivos -->
                    <section class="form-section">
                        <h2 class="section-title">Objetivos</h2>
                        <div class="form-group">
                            <label for="descriptionObjectives">Descripción de Objetivos *</label>
                            <textarea id="descriptionObjectives" v-model="formData.descriptionObjectives"
                                class="form-input form-textarea" required></textarea>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="objectivesMission">Misión *</label>
                                <textarea id="objectivesMission" v-model="formData.objectivesMission"
                                    class="form-input form-textarea" required></textarea>
                            </div>

                            <div class="form-group">
                                <label for="objectivesVision">Visión *</label>
                                <textarea id="objectivesVision" v-model="formData.objectivesVision"
                                    class="form-input form-textarea" required></textarea>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="objectivesValues">Valores *</label>
                            <textarea id="objectivesValues" v-model="formData.objectivesValues"
                                class="form-input form-textarea" required></textarea>
                        </div>

                        <div class="form-group">
                            <label for="objectivesSpecific">Objetivos Específicos *</label>
                            <textarea id="objectivesSpecific" v-model="formData.objectivesSpecific"
                                class="form-input form-textarea" required></textarea>
                        </div>
                    </section>

                    <!-- Alcance y Entregables -->
                    <section class="form-section">
                        <h2 class="section-title">Alcance y Entregables</h2>
                        <div class="form-group">
                            <label for="preliminaryScope">Alcance Preliminar *</label>
                            <textarea id="preliminaryScope" v-model="formData.preliminaryScope"
                                class="form-input form-textarea" required></textarea>
                        </div>

                        <div class="form-group">
                            <label for="deliverables">Entregables *</label>
                            <textarea id="deliverables" v-model="formData.deliverables" class="form-input form-textarea"
                                required></textarea>
                        </div>
                    </section>

                    <!-- Riesgos y Criterios de Éxito -->
                    <section class="form-section">
                        <h2 class="section-title">Riesgos y Criterios de Éxito</h2>
                        <div class="form-group">
                            <label for="preliminaryRisks">Riesgos Preliminares *</label>
                            <textarea id="preliminaryRisks" v-model="formData.preliminaryRisks"
                                class="form-input form-textarea" required></textarea>
                        </div>

                        <div class="form-group">
                            <label for="successCriteria">Criterios de Éxito *</label>
                            <textarea id="successCriteria" v-model="formData.successCriteria"
                                class="form-input form-textarea" required></textarea>
                        </div>
                    </section>

                    <!-- Hitos -->
                    <section class="form-section">
                        <h2 class="section-title">Hitos</h2>
                        <div class="form-group
                        ">
                            <label for="milestones">Hitos *</label>
                            <textarea id="milestones" v-model="formData.milestones" class="form-input form-textarea"
                                required></textarea>
                        </div>
                        <div class="form-row">
                            <div class="form-group">
                                <label for="functionalRequirements">Requisitos Funcionales *</label>
                                <textarea id="functionalRequirements" v-model="formData.functionalRequirements"
                                    class="form-input form-textarea" required></textarea>
                            </div>

                            <div class="form-group">
                                <label for="nonFunctionalRequirements">Requisitos No Funcionales *</label>
                                <textarea id="nonFunctionalRequirements" v-model="formData.nonFunctionalRequirements"
                                    class="form-input form-textarea" required></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="descriptionRequirements">Descripción de Requisitos *</label>
                            <textarea id="descriptionRequirements" v-model="formData.descriptionRequirements"
                                class="form-input form-textarea" required></textarea>
                        </div>
                    </section>

                    <!-- Roles y Responsabilidades -->
                    <section class="form-section">
                        <h2 class="section-title">Roles y Responsabilidades</h2>

                        <div v-for="(role, index) in formData.rolesAndResponsabilities" :key="index"
                            class="stakeholder-row">
                            <div
                                :class="['stakeholder-inputs', { 'invalid-section': isFieldInvalid(`role-${index}`) }]">
                                <div class="form-group">
                                    <label :for="'roleType' + index">Rol *</label>
                                    <input :id="'roleType' + index" v-model="role.Rol" type="text" class="form-input"
                                        placeholder="Ingrese el rol"
                                        @input="validateNoCommas($event, 'role', index, 'Rol')" required />
                                    <span v-if="hasComma(role.Rol)" class="invalid-feedback">No se permiten comas en
                                        este campo</span>
                                </div>

                                <div class="form-group">
                                    <label :for="'roleFunction' + index">Función *</label>
                                    <input :id="'roleFunction' + index" v-model="role.Funcion" type="text"
                                        class="form-input" placeholder="Ingrese la función"
                                        @input="validateNoCommas($event, 'role', index, 'Funcion')" required />
                                    <span v-if="hasComma(role.Funcion)" class="invalid-feedback">No se permiten comas en
                                        este campo</span>
                                </div>

                                <div class="form-group">
                                    <label :for="'roleResponsibility' + index">Responsabilidad *</label>
                                    <input :id="'roleResponsibility' + index" v-model="role.Responsabilidades"
                                        type="text" class="form-input" placeholder="Ingrese la responsabilidad"
                                        @input="validateNoCommas($event, 'role', index, 'Responsabilidades')"
                                        required />
                                    <span v-if="hasComma(role.Responsabilidades)" class="invalid-feedback">No se
                                        permiten comas en este campo</span>
                                </div>
                            </div>

                            <button v-if="formData.rolesAndResponsabilities.length > 1" type="button"
                                class="remove-stakeholder-button" @click="removeRole(index)">
                                ×
                            </button>
                        </div>

                        <div class="add-stakeholder-container">
                            <button type="button" class="add-stakeholder-button" @click="addRole">
                                +
                            </button>
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

                <div v-if="errorMessage" class="error-message">
                    {{ errorMessage }}
                </div>
                <div v-if="pdfUrl" class="pdf-container">
                    <div class="pdf-header">
                        <h2 class="pdf-title">Vista previa del documento</h2>
                        <div class="pdf-actions">
                            <a :href="pdfUrl" download="ActaConstitucion.pdf" class="pdf-button download-button">
                                <span class="button-icon">↓</span>
                                Descargar PDF
                            </a>
                        </div>
                    </div>

                    <div class="pdf-viewer">
                        <iframe :src="pdfUrl" class="pdf-iframe"></iframe>
                    </div>

                    <div class="pdf-footer">
                        <button @click="navigateTo('/ProjectManagementAreas')" class="pdf-button return-button">
                            Volver al Dashboard
                        </button>
                    </div>
                </div>
            </div>
        </main>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue';
import { useAuthStore } from '../stores/auth';
import { useProjectStore } from '../stores/project';
import { useConstitutionFormStore } from '../stores/ConstitutionForm';

const loading = ref(false);
const errorMessage = ref('');
const AuthStore = useAuthStore();
const ProjectStore = useProjectStore();
const ConstitutionFormStore = useConstitutionFormStore();
const pdfUrl = ref(null);

const formData = reactive({
    proyectName: '',
    idProyect: '',
    company: '',
    proyectPromotor: '',
    proyectLeader: '',
    elaborationDate: new Date().toISOString().split('T')[0],
    proyectPurpouse: '',
    proyectDescription: '',
    proyectStakeholders: [
        {
            type: '',
            position: '',
            name: ''
        }
    ],
    preliminaryScope: '',
    descriptionObjectives: '',
    objectivesMission: '',
    objectivesVision: '',
    objectivesValues: '',
    successCriteria: '',
    deliverables: '',
    objectivesSpecific: '',
    preliminaryRisks: '',
    milestones: '',
    rolesAndResponsabilities: [
        {
            Rol: '',
            Funcion: '',
            Responsabilidades: '',
        }
    ],
    descriptionRequirements: '',
    functionalRequirements: '',
    nonFunctionalRequirements: ''
});

const invalidFields = ref(new Set());

const validateForm = () => {
    invalidFields.value.clear();

    // Validar campos básicos
    Object.entries(formData).forEach(([key, value]) => {
        if (typeof value === 'string' && !value.trim()) {
            invalidFields.value.add(key);
        }
    });

    // Validar stakeholders
    formData.proyectStakeholders.forEach((stakeholder, index) => {
        if (!stakeholder.type || !stakeholder.position || !stakeholder.name) {
            invalidFields.value.add(`stakeholder-${index}`);
        }
    });

    // Validar roles
    formData.rolesAndResponsabilities.forEach((role, index) => {
        if (!role.Rol || !role.Funcion || !role.Responsabilidades) {
            invalidFields.value.add(`role-${index}`);
        }
    });

    // Agregar validación de comas
    formData.proyectStakeholders.forEach((stakeholder, index) => {
        if (hasComma(stakeholder.position) || hasComma(stakeholder.name)) {
            invalidFields.value.add(`stakeholder-${index}`);
        }
    });

    formData.rolesAndResponsabilities.forEach((role, index) => {
        if (hasComma(role.Rol) || hasComma(role.Funcion) || hasComma(role.Responsabilidades)) {
            invalidFields.value.add(`role-${index}`);
        }
    });

    return invalidFields.value.size === 0;
};

const showCancelConfirmation = ref(false);
const showSaveConfirmation = ref(false);

const handleCancel = () => {
    showCancelConfirmation.value = false;
    navigateTo('/principalView');
};

const addStakeholder = () => {
    formData.proyectStakeholders.push({
        type: '',
        position: '',
        name: ''
    });
};

const removeStakeholder = (index) => {
    formData.proyectStakeholders.splice(index, 1);
};

const addRole = () => {
    formData.rolesAndResponsabilities.push({
        Rol: '',
        Funcion: '',
        Responsabilidades: ''
    });
};

const removeRole = (index) => {
    formData.rolesAndResponsabilities.splice(index, 1);
};


const formatRolesToString = (roles) => {
    if (!roles || roles.length === 0) return '';

    let result = '&Número de Rol,Rol,Función,Responsabilidades&';

    roles.forEach((role, index) => {
        result += `${index + 1},${role.Rol},${role.Funcion},${role.Responsabilidades}&`;
    });

    return result;
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

        // Obtener la información del proyecto
        const respondeProyect = await fetch(`http://localhost:8080/api/v1/proyect/getById/${projectId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            }
        });

        //imprimo por consola la informacion del proyecto
        console.log(respondeProyect);

        // Obtener la información del usuario
        const respondeUser = await fetch(`http://localhost:8080/api/v1/user/getById/${userId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
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
        formData.company = projectDataResponse.organization;
        formData.elaborationDate = projectDataResponse.startDate;
        formData.proyectDescription = projectDataResponse.description;
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

        // Convertir los arrays al formato requerido
        const formattedStakeholders = formatStakeholdersToString(formData.proyectStakeholders);
        const formattedRoles = formatRolesToString(formData.rolesAndResponsabilities);

        // Ahora solo necesitamos hacer una única llamada al endpoint actualizado
        const responseConstitution = await fetch(`http://localhost:8080/api/documents/institution/generate?idProyecto=${projectId}`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                ...formData,
                proyectStakeholders: formattedStakeholders,
                rolesAndResponsabilities: formattedRoles,
            }),
        });

        if (!responseConstitution.ok) {
            const errorData = await responseConstitution.json();
            throw new Error(errorData.message || 'Error al crear el acta de constitución');
        }

        // Procesar la respuesta del PDF
        const pdfBlob = await responseConstitution.blob();
        pdfUrl.value = URL.createObjectURL(pdfBlob);

    } catch (error) {
        console.error('Error creating constitution act:', error);
        errorMessage.value = error.message || 'Error al crear el acta de constitución. Por favor, intenta nuevamente.';
    } finally {
        loading.value = false;
    }
};
// Función para formatear los stakeholders según la documentación
const formatStakeholdersToString = (stakeholders) => {
    if (!stakeholders || stakeholders.length === 0) return '';

    // Crear el encabezado
    let result = '&Número de Stakeholder,Tipo,Cargo,Nombre&';

    // Agregar cada stakeholder
    stakeholders.forEach((stakeholder, index) => {
        result += `${index + 1},${stakeholder.type},${stakeholder.position},${stakeholder.name}&`;
    });

    return result;
};

const confirmSave = async () => {
    try {
        loading.value = true;
        showSaveConfirmation.value = false;

        // Aquí implementarías la lógica para guardar los datos
        await handleSave();

        // Mostrar mensaje de éxito
        alert('Los datos se han guardado correctamente');
    } catch (error) {
        console.error('Error al guardar:', error);
        errorMessage.value = 'Error al guardar los datos. Por favor, intenta nuevamente.';
    } finally {
        loading.value = false;
    }
};

const handleSave = async () => {
    try {
        const userId = AuthStore.userId;
        const token = AuthStore.token;
        const projectId = ProjectStore.projectId;

        if (!userId || !token || !projectId) {
            alert('ALERTA: ¡Sesión no iniciada!, redirigiendo a login...')
            await navigateTo('/login')
            return;
        }

        // Convertir los arrays al formato requerido
        const formattedStakeholders = formatStakeholdersToString(formData.proyectStakeholders);
        const formattedRoles = formatRolesToString(formData.rolesAndResponsabilities);

        //llamada a la ruta: http://localhost:8080/api/v1/parameters/saveParametersList enviando (@RequestBody Map<String, String> data, @RequestParam Long idExit)

        const saveData = await fetch(`http://localhost:8080/api/v1/parameters/saveParametersList?idExit=${ProjectStore.projectId}`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                ...formData,
                proyectStakeholders: formattedStakeholders,
                rolesAndResponsabilities: formattedRoles,
            }),
        }); 

        if (!saveData.ok) {
            const errorData = await saveData.json();
            throw new Error(errorData.message || 'Error al guardar los datos');
        }

        return await saveData.json();
    } catch (error) {
        console.error('Error saving data:', error);
        throw error;
    }
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
        // Eliminar las comas del valor
        if (type === 'stakeholder') {
            formData.proyectStakeholders[index][field] = value.replace(/,/g, '');
        } else if (type === 'role') {
            formData.rolesAndResponsabilities[index][field] = value.replace(/,/g, '');
        }
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
</style>