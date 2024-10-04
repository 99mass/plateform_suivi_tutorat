import React, { useState, useEffect } from 'react';
import {
    Box,
    Button,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
    Typography,
    Dialog,
    DialogContent,
    DialogTitle,
    Snackbar,
    Alert,
} from '@mui/material';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import AddIcon from '@mui/icons-material/Add';
import SeanceService from '../../services/seancesService';
import UserService from '../../services/userService';
import ModuleService from '../../services/moduleService';
import GroupeService from '../../services/groupeService';

const theme = createTheme({
    palette: {
        primary: { main: '#1976d2' },
        secondary: { main: '#dc004e' },
    },
});

const AddSessionForm = ({ onClose, onSubmit }) => {
    const [session, setSession] = useState({
        tuteurId: '',
        moduleId: '',
        groupeId: '',
    });
    const [tuteurs, setTuteurs] = useState([]);
    const [modules, setModules] = useState([]);
    const [groupes, setGroupes] = useState([]);
    const [errors, setErrors] = useState({});
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');

    useEffect(() => {
        fetchTuteurs();
        fetchModules();
        fetchGroupes();
    }, []);

    const fetchTuteurs = async () => {
        try {
            const data = await UserService.listUsers('Tuteur');
            setTuteurs(data);
        } catch (error) {
            console.error("Error fetching tuteurs:", error);
            setSnackbarMessage("Erreur lors du chargement des tuteurs");
            setOpenSnackbar(true);
        }
    };

    const fetchModules = async () => {
        try {
            const data = await ModuleService.getAllModules();
            setModules(data);
        } catch (error) {
            console.error("Error fetching modules:", error);
            setSnackbarMessage("Erreur lors du chargement des modules");
            setOpenSnackbar(true);
        }
    };

    const fetchGroupes = async () => {
        try {
            const data = await GroupeService.getAllGroupes();
            setGroupes(data);
        } catch (error) {
            console.error("Error fetching groupes:", error);
            setSnackbarMessage("Erreur lors du chargement des groupes");
            setOpenSnackbar(true);
        }
    };

    const handleChange = (event) => {
        const { name, value } = event.target;
        setSession({ ...session, [name]: value });
        setErrors({ ...errors, [name]: '' });
    };

    const validateForm = () => {
        const newErrors = {};
        if (!session.tuteurId) newErrors.tuteurId = 'Le tuteur est requis';
        if (!session.moduleId) newErrors.moduleId = 'Le module est requis';
        if (!session.groupeId) newErrors.groupeId = 'Le groupe est requis';
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (validateForm()) {
            try {
                
                await SeanceService.createSeance(session.tuteurId, session.moduleId, session.groupeId);
                setSnackbarMessage('Séance ajoutée avec succès');
                setOpenSnackbar(true);
                onSubmit();
                onClose();
            } catch (error) {
                console.error("Error creating session:", error);
                setSnackbarMessage("Erreur lors de la création de la séance");
                setOpenSnackbar(true);
            }
        }
    };

    return (
        <ThemeProvider theme={theme}>
            <Box component="form" onSubmit={handleSubmit} sx={{ mt: 2 }}>
                <FormControl fullWidth margin="normal" error={!!errors.tuteurId}>
                    <InputLabel id="tuteur-label">Tuteur</InputLabel>
                    <Select
                        labelId="tuteur-label"
                        id="tuteurId"
                        name="tuteurId"
                        value={session.tuteurId}
                        onChange={handleChange}
                        label="Tuteur"
                    >
                        {tuteurs.map((tuteur) => (
                            <MenuItem key={tuteur.id} value={tuteur.id}>{tuteur.prenom} {tuteur.nom}</MenuItem>
                        ))}
                    </Select>
                    {errors.tuteurId && <Typography color="error">{errors.tuteurId}</Typography>}
                </FormControl>
                <FormControl fullWidth margin="normal" error={!!errors.moduleId}>
                    <InputLabel id="module-label">Module</InputLabel>
                    <Select
                        labelId="module-label"
                        id="moduleId"
                        name="moduleId"
                        value={session.moduleId}
                        onChange={handleChange}
                        label="Module"
                    >
                        {modules.map((module) => (
                            <MenuItem key={module.id} value={module.id}>{module.nom}</MenuItem>
                        ))}
                    </Select>
                    {errors.moduleId && <Typography color="error">{errors.moduleId}</Typography>}
                </FormControl>
                <FormControl fullWidth margin="normal" error={!!errors.groupeId}>
                    <InputLabel id="groupe-label">Groupe</InputLabel>
                    <Select
                        labelId="groupe-label"
                        id="groupeId"
                        name="groupeId"
                        value={session.groupeId}
                        onChange={handleChange}
                        label="Groupe"
                    >
                        {groupes.map((groupe) => (
                            <MenuItem key={groupe.id} value={groupe.id}>{groupe.nom}</MenuItem>
                        ))}
                    </Select>
                    {errors.groupeId && <Typography color="error">{errors.groupeId}</Typography>}
                </FormControl>
                <Box sx={{ mt: 3, display: 'flex', justifyContent: 'flex-end' }}>
                    <Button onClick={onClose} sx={{ mr: 1 }}>
                        Annuler
                    </Button>
                    <Button type="submit" variant="contained" startIcon={<AddIcon />}>
                        Ajouter la séance
                    </Button>
                </Box>
            </Box>
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

const AddSessionButton = ({ onSessionAdded }) => {
    const [open, setOpen] = useState(false);

    const handleOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleSubmit = () => {
        onSessionAdded();
        handleClose();
    };

    return (
        <>
            <Button
                variant="contained"
                startIcon={<AddIcon />}
                onClick={handleOpen}
            >
                Ajouter une séance
            </Button>
            <Dialog open={open} onClose={handleClose} maxWidth="sm" fullWidth>
                <DialogTitle sx={{
                    display: 'flex',
                    alignItems: 'center',
                    gap: 1,
                    color: 'primary.main',
                    fontWeight: 'bold'
                }}>
                    <AddIcon />
                    Ajouter une nouvelle séance
                </DialogTitle>
                <DialogContent>
                    <AddSessionForm onClose={handleClose} onSubmit={handleSubmit} />
                </DialogContent>
            </Dialog>
        </>
    );
};

export default AddSessionButton;