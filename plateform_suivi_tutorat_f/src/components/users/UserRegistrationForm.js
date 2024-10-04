import React, { useState } from 'react';
import {
    Box,
    TextField,
    Button,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
    Typography,
    Paper,
    Snackbar,
    Container,
    Alert,
} from '@mui/material';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import UserService from '../../services/userService';
import { Grid } from '@mui/system';

const theme = createTheme({
    palette: {
        primary: {
            main: '#1976d2',
        },
        secondary: {
            main: '#dc004e',
        },
    },
});

const UserRegistrationForm = ({ userData }) => {
    const [formData, setFormData] = useState({
        nom: '',
        prenom: '',
        email: '',
        telephone: '',
        motDePasse: '',
        role: 'tuteur',
    });

    const [errors, setErrors] = useState({});
    const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' });

    const validateForm = () => {
        const newErrors = {};

        if (!formData.nom.trim()) newErrors.nom = 'Le nom est requis';
        if (!formData.prenom.trim()) newErrors.prenom = 'Le prénom est requis';
        if (!formData.email.trim()) {
            newErrors.email = 'L\'email est requis';
        } else if (!/\S+@\S+\.\S+/.test(formData.email.trim())) {
            newErrors.email = 'L\'email est invalide';
        }


        if (!formData.telephone.trim()) newErrors.prenom = 'Le numéro telephone est requis';
        if (!formData.telephone.trim().length > 9) newErrors.telephone = 'Le numéro telephone doit etre exactement 9 chiffres';

        if (!formData.role.trim()) newErrors.role = 'Le rôle est requis';

        if (formData.telephone.trim() && !/^\d+$/.test(formData.telephone.trim())) {
            newErrors.telephone = 'Le numéro de téléphone doit contenir uniquement des chiffres';
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        if (validateForm()) {
            try {
                const trimmedData = Object.fromEntries(
                    Object.entries(formData).map(([key, value]) => [key, typeof value === 'string' ? value.trim() : value])
                );

                await UserService.createUser(trimmedData);
                setSnackbar({ open: true, message: 'Utilisateur créé avec succès!', severity: 'success' });
                // Reset form after successful submission
                setFormData({
                    nom: '',
                    prenom: '',
                    email: '',
                    telephone: '',
                    motDePasse: '',
                    role: 'tuteur',
                });
            } catch (error) {
                if (error.status === 403) {
                    setSnackbar({ open: true, message: 'Erreur lors de la création de l\'utilisateur. Vous n\'etes pas autoriser a effectuer cette action.', severity: 'error' });
                } else {
                    setSnackbar({ open: true, message: 'Erreur lors de la création de l\'utilisateur. Verifier si email est deja enregistrer.', severity: 'error' });
                }
            }
        } else {
            setSnackbar({ open: true, message: 'Veuillez corriger les erreurs dans le formulaire', severity: 'error' });
        }
    };

    const handleChange = (event) => {
        const { name, value } = event.target;
        setFormData(prevData => ({
            ...prevData,
            [name]: value,
        }));
        setErrors(prevErrors => ({ ...prevErrors, [name]: '' }));
    };

    const handleCloseSnackbar = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setSnackbar({ ...snackbar, open: false });
    };

    return (
        <ThemeProvider theme={theme}>
            <Container component="main" maxWidth="sm">
                <Paper elevation={3} sx={{ mb: 8, p: 4 }}>
                    <Box
                        sx={{
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            mb: 3,
                        }}
                    >
                        <PersonAddIcon sx={{ fontSize: 40, mr: 1, color: 'primary.main' }} />
                        <Typography component="h2" variant="h5" fontWeight='bold'>
                            Ajouter un nouvel utilisateur
                        </Typography>
                    </Box>
                    <Box component="form" onSubmit={handleSubmit} noValidate>
                        <Grid container spacing={2}>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    required
                                    fullWidth
                                    id="nom"
                                    label="Nom"
                                    name="nom"
                                    autoComplete="family-name"
                                    value={formData.nom}
                                    onChange={handleChange}
                                    error={!!errors.nom}
                                    helperText={errors.nom}
                                />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    required
                                    fullWidth
                                    id="prenom"
                                    label="Prénom"
                                    name="prenom"
                                    autoComplete="given-name"
                                    value={formData.prenom}
                                    onChange={handleChange}
                                    error={!!errors.prenom}
                                    helperText={errors.prenom}
                                />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    required
                                    fullWidth
                                    id="email"
                                    label="Adresse email"
                                    name="email"
                                    autoComplete="email"
                                    value={formData.email}
                                    onChange={handleChange}
                                    error={!!errors.email}
                                    helperText={errors.email}
                                />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    required
                                    type='number'
                                    fullWidth
                                    id="telephone"
                                    label="Téléphone"
                                    name="telephone"
                                    autoComplete="tel"
                                    value={formData.telephone}
                                    onChange={handleChange}
                                    error={!!errors.telephone}
                                    helperText={errors.telephone}
                                />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    fullWidth
                                    name="motDePasse"
                                    label="Mot de passe (Optionnel)"
                                    type="password"
                                    id="motDePasse"
                                    autoComplete="new-password"
                                    value={formData.motDePasse}
                                    onChange={handleChange}
                                    error={!!errors.motDePasse}
                                    helperText={errors.motDePasse}
                                    disabled
                                />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <FormControl fullWidth error={!!errors.role}>
                                    <InputLabel id="role-label">Rôle</InputLabel>
                                    <Select
                                        labelId="role-label"
                                        id="role"
                                        name="role"
                                        value={formData && formData.role}
                                        label="Rôle"
                                        onChange={handleChange}
                                    >
                                        {userData && userData.role === "admin" && <MenuItem value="admin">Admin</MenuItem>}
                                        {userData && userData.role === "admin" && <MenuItem value="tracker">Tracker</MenuItem>}
                                        <MenuItem value="tuteur">Tuteur</MenuItem>
                                    </Select>
                                    {errors.role && <Typography color="error" variant="caption">{errors.role}</Typography>}
                                </FormControl>
                            </Grid>
                        </Grid>
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2, py: 2 }}
                        >
                            Inscrire
                        </Button>
                    </Box>
                </Paper>
            </Container>
            <Snackbar
                open={snackbar.open}
                autoHideDuration={6000}
                onClose={handleCloseSnackbar}
                anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
            >
                <Alert onClose={handleCloseSnackbar} severity={snackbar.severity} sx={{ width: '100%' }}>
                    {snackbar.message}
                </Alert>
            </Snackbar>
        </ThemeProvider>
    );
};

export default UserRegistrationForm;