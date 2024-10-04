import React, { useState, useEffect } from 'react';
import {
    Box,
    TextField,
    Button,
} from '@mui/material';

const ModuleForm = ({ initialData, handleSaveEdit, onClose, handleAddModule,apiError, setApiError }) => {
    const [formData, setFormData] = useState({
        nom: '',
        nombreSemaines: '',
    });

    const [errors, setErrors] = useState({});
    const [loading, setLoading] = useState(false);
    

    useEffect(() => {
        if (initialData) {
            setFormData(initialData);
        }
    }, [initialData]);

    const validateForm = () => {
        const newErrors = {};

        if (!formData.nom.trim()) newErrors.nom = 'Le nom du module est requis';
        if (!formData.nombreSemaines) {
            newErrors.nombreSemaines = 'Le nombre de semaines est requis';
        } else if (parseInt(formData.nombreSemaines) < 1) {
            newErrors.nombreSemaines = 'Le nombre de semaines doit être supérieur ou égal à 1';
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (validateForm()) {
            setLoading(true);
            setApiError('');

            const formDataToSubmit = {
                ...formData,
                nombreSemaines: parseInt(formData.nombreSemaines, 10), // Conversion en entier
            };

            try {
                if (initialData) {
                    await handleSaveEdit(formDataToSubmit);
                } else {
                    await handleAddModule(formDataToSubmit);
                }
                setLoading(false);
            } catch (error) {
                console.error("Erreur lors de l'ajout/modification du module:", error);
                setApiError(error.response?.data?.message || "Erreur, veuillez vérifier si le module existe déjà ou réessayer.");
                setLoading(false);
                throw error; // Propagez l'erreur
            }
        }
    };

    const handleChange = (event) => {
        const { name, value } = event.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    return (
        <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 2 }}>
            <TextField
                margin="normal"
                required
                fullWidth
                id="nom"
                label="Nom du module"
                name="nom"
                value={formData.nom}
                onChange={handleChange}
                error={!!errors.nom}
                helperText={errors.nom}
            />
            <TextField
                margin="normal"
                required
                fullWidth
                id="nombreSemaines"
                label="Nombre de semaines"
                name="nombreSemaines"
                type="number"
                inputProps={{ min: 1 }}
                value={formData.nombreSemaines}
                onChange={handleChange}
                error={!!errors.nombreSemaines}
                helperText={errors.nombreSemaines}
            />
            {/* Affichage d'un message d'erreur si l'API renvoie une erreur */}
            {apiError && <p style={{ color: 'red' }}>{apiError}</p>}

            <Box sx={{ display: 'flex', justifyContent: 'flex-end', mt: 3 }}>
                <Button onClick={onClose} sx={{ mr: 2 }} disabled={loading}>
                    Annuler
                </Button>
                <Button type="submit" variant="contained" disabled={loading}>
                    {initialData ? 'Modifier' : 'Ajouter'} le module
                </Button>
            </Box>
        </Box>
    );
};

export default ModuleForm;
