<template>
    <div class="page-container">
        <header class="header">
            <div class="logo">
                <img src="../assets/usach-logo.png" alt="USACH" class="usach-logo" />
            </div>
        </header>

        <main class="main-content">
            <div class="management-container">
                <h1 class="section-title">Gestión del alcance</h1>
                <p class="section-subtitle">Selecciona que área quieres abordar.</p>

                <div class="management-areas">
                    <button v-for="area in managementAreas" :key="area.id" class="area-button"
                        @click="handleAreaClick(area)">
                        <span class="area-number">{{ area.id }}.</span>
                        <span class="area-title">{{ area.title }}</span>
                    </button>
                </div>
            </div>
        </main>
    </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/auth';
import { useProjectStore } from '../stores/project';
import { useManagementsStore } from '../stores/Managements';
import { useProcessStore } from '../stores/Process';

const router = useRouter();
const AuthStore = useAuthStore();
const ProjectStore = useProjectStore();
const ManagementsStore = useManagementsStore();
const ProcessStore = useProcessStore();
const { fetch } = useFetchWithAuth();

const managementAreas = [
    {
        id: 1,
        title: 'Plan de gestión del alcance',
        route: '/scope-planning'
    },
    {
        id: 2,
        title: 'Plan de gestión de los requisitos',
        route: '/requirements'
    },
    {
        id: 3,
        title: 'Documentación de los requisitos',
        route: '/scope-definition'
    },
    {
        id: 4,
        title: 'Estructura de desglose del trabajo (EDT)',
        route: '/wbs'
    },
    {
        id: 5,
        title: 'Enunciado del alcance del proyecto',
        route: '/ScopeStatement'
    },
    {
        id: 6,
        title: 'Línea base del alcance',
        route: '/scope-control'
    },
    {
        id: 7,
        title: 'Entregables validados',
        route: '/validated-deliverables'
    },
    {
        id: 8,
        title: 'Informes de desempeño del trabajo',
        route: '/work-performance-reports'
    },
    {
        id: 9,
        title: 'Documentacion y matriz de trazabilidad',
        route: '/DocumentationTraceabilityMatrix'
    }
];

const handleAreaClick = async (area) => {
    try {
        const token = AuthStore.token;
        const idProyecto = ProjectStore.projectId;
        const idGestion = ManagementsStore.managementId;

        if (!token || !idProyecto || !idGestion) {
            console.error('No hay token, idProyecto o idGestion');
            router.push('/login');
            return;
        }

        // Verificamos si ya existe el área
        const response = await fetch(`http://localhost:8080/api/v1/process/getByIdManagementAndNameProcess?idManagement=${idGestion}&nameProcess=${area.title}`, {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        });

        if (response.ok) {
            const process = await response.json();
            ProcessStore.processId = process.idProcess;
            ProcessStore.processName = process.nameProcess;
            console.log('Proceso encontrado:', process);
            router.push(area.route);
        } else if (response.status === 404) {
            // Formatear la fecha actual en YYYY-MM-DD
            const currentDate = new Date().toISOString().split('T')[0];

            // Si no existe, creamos uno nuevo
            const createResponse = await fetch('http://localhost:8080/api/v1/process/create', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    idManagement: idGestion,
                    nameProcess: area.title,
                    description: area.title,
                    stateProcess: "Activo",
                    startDatePlanned: currentDate,
                    endDatePlanned: currentDate,
                    startDateReal: currentDate,
                    endDateReal: currentDate
                }),
            });

            if (createResponse.ok) {
                const newProcess = await createResponse.json();
                ProcessStore.processId = newProcess.idProcess;
                ProcessStore.processName = newProcess.nameProcess;
                console.log('Proceso creado:', newProcess);
                router.push(area.route);
            } else {
                console.error('Error al crear el proceso');
            }
        }
    } catch (error) {
        console.error('Error en la gestión del proceso:', error);
    }
};
</script>

<style scoped>
.page-container {
    min-height: 100vh;
    background-color: #f5f5f5;
}

.header {
    background-color: #00B8B0;
    padding: 1rem 2rem;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.usach-logo {
    height: 40px;
}

.main-content {
    padding: 2rem;
    max-width: 1200px;
    margin: 0 auto;
}

.management-container {
    background: white;
    border-radius: 8px;
    padding: 2rem;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.section-title {
    font-size: 1.5rem;
    color: #333;
    margin-bottom: 0.5rem;
    font-weight: bold;
}

.section-subtitle {
    color: #666;
    margin-bottom: 2rem;
}

.management-areas {
    display: flex;
    flex-direction: column;
    gap: 1rem;
}

.area-button {
    display: flex;
    align-items: center;
    width: 100%;
    padding: 1rem;
    background-color: #00B8B0;
    color: white;
    border: none;
    border-radius: 25px;
    text-align: left;
    cursor: pointer;
    transition: background-color 0.2s;
    font-size: 1rem;
}

.area-button:hover {
    background-color: #009B94;
}

.area-number {
    margin-right: 1rem;
}

.area-title {
    flex: 1;
}

@media (max-width: 768px) {
    .main-content {
        padding: 1rem;
    }

    .management-container {
        padding: 1.5rem;
    }

    .area-button {
        padding: 0.75rem;
        font-size: 0.9rem;
    }
}
</style>