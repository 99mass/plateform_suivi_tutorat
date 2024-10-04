import React, { useState, useEffect } from 'react';
import {
    Typography,
    Box,
    TextField,
    InputAdornment,
    Snackbar,
    Alert,
    Grid,
    Button,
    Modal,
    Divider,
    CircularProgress,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
} from '@mui/material';

import {
    Search as SearchIcon,
    EventNote as EventNoteIcon,
    Check as CheckIcon,
    Close as CloseIcon,
} from '@mui/icons-material';

import { ThemeProvider } from '@mui/material/styles';
import AddSessionButton from './AddSessionForm';
import SessionCard from './SessionCard';
import theme from '../../utils/theme';
import SeanceService from '../../services/seancesService';
import UserService from '../../services/userService';
import ModuleService from '../../services/moduleService';
import GroupeService from '../../services/groupeService';

const SessionManagement = () => {
    const [sessions, setSessions] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');
    const [selectedSessions, setSelectedSessions] = useState([]);
    const [openModal, setOpenModal] = useState(false);
    const [openEditModal, setOpenEditModal] = useState(false);
    const [editingSessions, setEditingSessions] = useState([]);
    const [currentEditIndex, setCurrentEditIndex] = useState(0);
    const [openMarkSessionModal, setOpenMarkSessionModal] = useState(false);
    const [currentMarkingSession, setCurrentMarkingSession] = useState(null);
    const [tuteurs, setTuteurs] = useState([]);
    const [modules, setModules] = useState([]);
    const [groupes, setGroupes] = useState([]);
    const [apiError, setApiError] = useState('');

    useEffect(() => {
        fetchSessions();
        fetchTuteurs();
        fetchModules();
        fetchGroupes();
    }, []);

    const fetchSessions = async () => {
        try {
            setLoading(true);
            const data = await SeanceService.getAllSeances();
            setSessions(data);
        } catch (error) {
            console.error("Error fetching sessions:", error);
            setError("Une erreur est survenue lors du chargement des séances.");
        } finally {
            setLoading(false);
        }
    };

    const fetchTuteurs = async () => {
        try {
            const data = await UserService.listUsers('Tuteur');
            setTuteurs(data);
        } catch (error) {
            console.error("Error fetching tuteurs:", error);
        }
    };

    const fetchModules = async () => {
        try {
            const data = await ModuleService.getAllModules();
            setModules(data);
        } catch (error) {
            console.error("Error fetching modules:", error);
        }
    };

    const fetchGroupes = async () => {
        try {
            const data = await GroupeService.getAllGroupes();
            setGroupes(data);
        } catch (error) {
            console.error("Error fetching groupes:", error);
        }
    };

    const filteredSessions = sessions.filter((session) =>
        session &&
        ((session.tuteur?.nom && session.tuteur.nom.toLowerCase().includes(searchTerm.toLowerCase())) ||
            (session.module?.nom && session.module.nom.toLowerCase().includes(searchTerm.toLowerCase())) ||
            (session.groupe?.nom && session.groupe.nom.toLowerCase().includes(searchTerm.toLowerCase())) ||
            (session.date && new Date(session.date).toLocaleDateString().toLowerCase().includes(searchTerm.toLowerCase())))
    );

    const handleSearch = (event) => {
        setSearchTerm(event.target.value);
    };

    const handleSelect = (sessionId) => {
        setSelectedSessions(prev =>
            prev.includes(sessionId)
                ? prev.filter(id => id !== sessionId)
                : [...prev, sessionId]
        );
    };

    const handleOpenModal = () => {
        setOpenModal(true);
    };

    const handleCloseModal = () => {
        setOpenModal(false);
    };

    const handleEdit = () => {
        const sessionsToEdit = sessions.filter(s => selectedSessions.includes(s.id));
        setEditingSessions(sessionsToEdit.map(session => ({
            ...session,
            tuteurId: session.tuteur?.id || '',
            moduleId: session.module?.id || '',
            groupeId: session.groupe?.id || '',
        })));
        setApiError('')
        setCurrentEditIndex(0);
        setOpenEditModal(true);
        handleCloseModal();
    };
    

    const handleDelete = async () => {
        try {
            for (const sessionId of selectedSessions) {
                await SeanceService.deleteSeance(sessionId);
            }
            await fetchSessions();
            setSnackbarMessage(`${selectedSessions.length} séance(s) supprimée(s) avec succès`);
            setOpenSnackbar(true);
            setSelectedSessions([]);
        } catch (error) {
            console.error("Error deleting sessions:", error);
            setSnackbarMessage("Une erreur est survenue lors de la suppression des séances.");
            setOpenSnackbar(true);
        }
        handleCloseModal();
    };

    const handleEditSubmit = async (editedSession) => {
        try {
            const { id, tuteurId, moduleId, groupeId, date, heuresEffectuees, heuresNonEffectuees } = editedSession;
            
            if (!date || !tuteurId || !moduleId || !groupeId) {
                throw new Error("Tous les champs (date,tuteur, module, groupe) doivent être remplis.");
            }

            const seanceData = {
                tuteur: { id: tuteurId },
                module: { id: moduleId },
                groupe: { id: groupeId },
                date: date,
                heuresEffectuees: heuresEffectuees,
                heuresNonEffectuees: heuresNonEffectuees, 
                effectuee: false 
            };
            
            setApiError('')
            await SeanceService.updateSeance(seanceData, id);
            await fetchSessions();

            if (currentEditIndex < editingSessions.length - 1) {
                setCurrentEditIndex(currentEditIndex + 1);
            } else {
                setOpenEditModal(false);
                setSnackbarMessage(`${editingSessions.length} séance(s) modifiée(s) avec succès`);
                setOpenSnackbar(true);
                setSelectedSessions([]);
                setEditingSessions([]);
                setCurrentEditIndex(0);
            }
        } catch (error) {
            console.error("Error updating session:", error);
            setApiError(error.response?.data?.message || "Une erreur est survenue lors de la mise à jour de la séance.");
        }
    };

    const handleMarkSession = (sessionId) => {
        setCurrentMarkingSession(sessions.find(s => s.id === sessionId));
        setOpenMarkSessionModal(true);
    };

    const handleMarkSessionSubmit = async (effectuee) => {
        try {
            await SeanceService.markedSeanceStatus(currentMarkingSession.id, effectuee);
            await fetchSessions();
            setOpenMarkSessionModal(false);
            setSnackbarMessage(`Séance marquée comme ${effectuee ? 'effectuée' : 'non effectuée'}`);
            setOpenSnackbar(true);
        } catch (error) {
            console.error("Error marking session:", error);
            setSnackbarMessage("Une erreur est survenue lors du marquage de la séance.");
            setOpenSnackbar(true);
        }
    };

    if (loading) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
                <CircularProgress />
            </Box>
        );
    }

    if (error) {
        return (
            <Box sx={{ p: 3, textAlign: 'center' }}>
                <Typography variant="h6" color="error">{error}</Typography>
            </Box>
        );
    }

    return (
        <ThemeProvider theme={theme}>
            <Box sx={{ p: 3, bgcolor: 'background.default', minHeight: '100vh' }}>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 4 }}>
                    <Typography variant="h4" component="h1" sx={{
                        fontSize: { xs: 24, sm: 28, md: 32 },
                        fontWeight: 'bold',
                        display: 'flex',
                        alignItems: 'center',
                        color: 'primary.main',
                    }}>
                        <EventNoteIcon sx={{ fontSize: { xs: 32, sm: 36, md: 40 }, mr: 2 }} />
                        Gestion des Séances
                    </Typography>
                </Box>

                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 4 }}>
                    <TextField
                        sx={{ width: '30%' }}
                        variant="outlined"
                        placeholder="Rechercher une séance..."
                        value={searchTerm}
                        onChange={handleSearch}
                        InputProps={{
                            startAdornment: (
                                <InputAdornment position="start">
                                    <SearchIcon />
                                </InputAdornment>
                            ),
                        }}
                    />
                    <Box>
                        <Button
                            variant="contained"
                            color="primary"
                            onClick={handleOpenModal}
                            disabled={selectedSessions.length === 0}
                            sx={{ mr: 2 }}
                        >
                            Gérer séances ({selectedSessions.length})
                        </Button>
                        <AddSessionButton onSessionAdded={fetchSessions} />
                    </Box>
                </Box>
                <Divider sx={{ mb: 3 }} />
                {filteredSessions.length === 0 ? (
                    <Typography variant="body1" sx={{ textAlign: 'center', mt: 4 }}>
                        Aucune séance trouvée.
                    </Typography>
                ) : (
                    <Grid container spacing={3}>
                        {filteredSessions.map((session) => (
                            <Grid item xs={12} sm={6} md={4} lg={3} key={session.id}>
                                <SessionCard
                                    session={session}
                                    isSelected={selectedSessions.includes(session.id)}
                                    onSelect={handleSelect}
                                    onMarkSession={handleMarkSession}
                                />
                            </Grid>
                        ))}
                    </Grid>
                )}
            </Box>

            <Modal
                open={openModal}
                onClose={handleCloseModal}
                aria-labelledby="modal-title"
                aria-describedby="modal-description"
            >
                <Box sx={{
                    position: 'absolute',
                    top: '50%',
                    left: '50%',
                    transform: 'translate(-50%, -50%)',
                    width: 400,
                    bgcolor: 'background.paper',
                    boxShadow: 24,
                    p: 4,
                    borderRadius: 2,
                }}>
                    <Typography id="modal-title" variant="h6" component="h2" gutterBottom>
                        Gérer les séances
                    </Typography>
                    <Typography id="modal-description" sx={{ mt: 2 }}>
                        {selectedSessions.length} séance(s) sélectionnée(s)
                    </Typography>
                    <Box sx={{ mt: 3, display: 'flex', justifyContent: 'space-between' }}>
                        <Button onClick={handleEdit} variant="contained" color="primary">
                            Modifier
                        </Button>
                        <Button onClick={handleDelete} variant="contained" color="secondary">
                            Supprimer
                        </Button>
                    </Box>
                </Box>
            </Modal>

            <Modal
                open={openEditModal}
                onClose={() => setOpenEditModal(false)}
                aria-labelledby="edit-modal-title"
                aria-describedby="edit-modal-description"
            >
                <Box sx={{
                    position: 'absolute',
                    top: '50%',
                    left: '50%',
                    transform: 'translate(-50%, -50%)',
                    width: 400,
                    bgcolor: 'background.paper',
                    boxShadow: 24,
                    p: 4,
                    borderRadius: 2,
                }}>
                    <Typography id="edit-modal-title" variant="h6" component="h2" gutterBottom>
                        Modifier la séance {currentEditIndex + 1} sur {editingSessions.length}
                    </Typography>
                    {editingSessions[currentEditIndex] && (
                        <Box component="form" onSubmit={(e) => {
                            e.preventDefault();
                            handleEditSubmit(editingSessions[currentEditIndex]);
                        }}>
                            <TextField
                                fullWidth
                                label="Date"
                                type="datetime-local"
                                value={editingSessions[currentEditIndex].date ? new Date(editingSessions[currentEditIndex].date).toISOString().slice(0, 16) : ''}
                                onChange={(e) => {
                                    const updatedSessions = [...editingSessions];
                                    updatedSessions[currentEditIndex] = {
                                        ...updatedSessions[currentEditIndex],
                                        date: new Date(e.target.value).toISOString()
                                    };
                                    setEditingSessions(updatedSessions);
                                }}
                                margin="normal"
                                InputLabelProps={{
                                    shrink: true,
                                }}
                            />
                            <FormControl fullWidth margin="normal">
                                <InputLabel id="tuteur-label">Tuteur</InputLabel>
                                <Select
                                    labelId="tuteur-label"
                                    value={editingSessions[currentEditIndex].tuteurId}
                                    onChange={(e) => {
                                        const updatedSessions = [...editingSessions];
                                        updatedSessions[currentEditIndex] = {
                                            ...updatedSessions[currentEditIndex],
                                            tuteurId: e.target.value
                                        };
                                        setEditingSessions(updatedSessions);
                                    }}
                                    label="Tuteur"
                                >
                                    {tuteurs.map((tuteur) => (
                                        <MenuItem key={tuteur.id} value={tuteur.id}>{tuteur.prenom} {tuteur.nom}</MenuItem>
                                    ))}
                                </Select>
                            </FormControl>
                            <FormControl fullWidth margin="normal">
                                <InputLabel id="module-label">Module</InputLabel>
                                <Select
                                    labelId="module-label"
                                    value={editingSessions[currentEditIndex].moduleId}
                                    onChange={(e) => {
                                        const updatedSessions = [...editingSessions];
                                        updatedSessions[currentEditIndex] =  {
                                            ...updatedSessions[currentEditIndex],
                                            moduleId: e.target.value
                                        };
                                        setEditingSessions(updatedSessions);
                                    }}
                                    label="Module"
                                >
                                    {modules.map((module) => (
                                        <MenuItem key={module.id} value={module.id}>{module.nom}</MenuItem>
                                    ))}
                                </Select>
                            </FormControl>
                            <FormControl fullWidth margin="normal">
                                <InputLabel id="groupe-label">Groupe</InputLabel>
                                <Select
                                    labelId="groupe-label"
                                    value={editingSessions[currentEditIndex].groupeId}
                                    onChange={(e) => {
                                        const updatedSessions = [...editingSessions];
                                        updatedSessions[currentEditIndex] = {
                                            ...updatedSessions[currentEditIndex],
                                            groupeId: e.target.value
                                        };
                                        setEditingSessions(updatedSessions);
                                    }}
                                    label="Groupe"
                                >
                                    {groupes.map((groupe) => (
                                        <MenuItem key={groupe.id} value={groupe.id}>{groupe.nom}</MenuItem>
                                    ))}
                                </Select>
                            </FormControl>
                            <TextField
                                fullWidth
                                label="Heures effectuées"
                                type="number"
                                value={editingSessions[currentEditIndex].heuresEffectuees ?? 0}
                                onChange={(e) => {
                                    const updatedSessions = [...editingSessions];
                                    updatedSessions[currentEditIndex] = {
                                        ...updatedSessions[currentEditIndex],
                                        heuresEffectuees: Number(e.target.value) || 0
                                    };
                                    setEditingSessions(updatedSessions);
                                }}
                                margin="normal"
                            />
                            <TextField
                                fullWidth
                                label="Heures non effectuées"
                                type="number"
                                value={editingSessions[currentEditIndex].heuresNonEffectuees ?? 0}
                                onChange={(e) => {
                                    const updatedSessions = [...editingSessions];
                                    updatedSessions[currentEditIndex] = {
                                        ...updatedSessions[currentEditIndex],
                                        heuresNonEffectuees: Number(e.target.value) || 0
                                    };
                                    setEditingSessions(updatedSessions);
                                }}
                                margin="normal"
                            />
                             {apiError && <p style={{ color: 'red' }}>{apiError}</p>}
                            <Box sx={{ mt: 3, display: 'flex', justifyContent: 'flex-end' }}>
                                <Button type="submit" variant="contained" color="primary">
                                    {currentEditIndex < editingSessions.length - 1 ? 'Suivant' : 'Terminer'}
                                </Button>
                            </Box>
                        </Box>
                    )}
                </Box>
            </Modal>

            <Modal
                open={openMarkSessionModal}
                onClose={() => setOpenMarkSessionModal(false)}
                aria-labelledby="mark-session-modal-title"
                aria-describedby="mark-session-modal-description"
            >
                <Box sx={{
                    position: 'absolute',
                    top: '50%',
                    left: '50%',
                    transform: 'translate(-50%, -50%)',
                    width: 400,
                    bgcolor: 'background.paper',
                    boxShadow: 24,
                    p: 4,
                    borderRadius: 2,
                }}>
                    <Typography id="mark-session-modal-title" variant="h6" component="h2" gutterBottom>
                        Marquer la séance
                    </Typography>
                    <Typography id="mark-session-modal-description" sx={{ mt: 2, mb: 3 }}>
                        Voulez-vous marquer cette séance comme effectuée ou non effectuée ?
                    </Typography>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                        <Button
                            onClick={() => handleMarkSessionSubmit(true)}
                            variant="contained"
                            color="success"
                            startIcon={<CheckIcon />}
                        >
                            Effectuée
                        </Button>
                        <Button
                            onClick={() => handleMarkSessionSubmit(false)}
                            variant="contained"
                            color="error"
                            startIcon={<CloseIcon />}
                        >
                            Non effectuée
                        </Button>
                    </Box>
                </Box>
            </Modal>

            <Snackbar
                open={openSnackbar}
                autoHideDuration={3000}
                onClose={() => setOpenSnackbar(false)}
                anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
            >
                <Alert onClose={() => setOpenSnackbar(false)} severity="success" sx={{ width: '100%' }}>
                    {snackbarMessage}
                </Alert>
            </Snackbar>
        </ThemeProvider>
    );
};

export default SessionManagement;