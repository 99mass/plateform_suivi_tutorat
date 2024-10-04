import React, { useState } from 'react';
import {
  Box,
  Typography,
  Grid,
  Card,
  CardContent,
  CardActions,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  MenuItem,
  Snackbar,
  Alert
} from '@mui/material';
import { 
  BarChart, 
  PieChart, 
  Timeline, 
  Group, 
  School, 
  CalendarToday 
} from '@mui/icons-material';

const reportTypes = [
  { title: "Performances des tuteurs", icon: <BarChart fontSize="large" color="primary" />, description: "Analyse détaillée des performances individuelles des tuteurs" },
  { title: "Répartition des groupes", icon: <PieChart fontSize="large" color="secondary" />, description: "Vue d'ensemble de la répartition des étudiants par groupe" },
  { title: "Évolution des séances", icon: <Timeline fontSize="large" color="success" />, description: "Suivi de l'évolution des séances au fil du temps" },
  { title: "Statistiques des étudiants", icon: <Group fontSize="large" color="info" />, description: "Données statistiques sur la participation et les progrès des étudiants" },
  { title: "Efficacité des modules", icon: <School fontSize="large" color="warning" />, description: "Évaluation de l'efficacité des différents modules d'enseignement" },
  { title: "Planification des séances", icon: <CalendarToday fontSize="large" color="error" />, description: "Aperçu de la planification et de la répartition des séances" },
];

const ReportsInterface = () => {
  const [openDialog, setOpenDialog] = useState(false);
  const [selectedReport, setSelectedReport] = useState(null);
  const [dateRange, setDateRange] = useState({ start: '', end: '' });
  const [openSnackbar, setOpenSnackbar] = useState(false);

  const handleGenerateReport = (report) => {
    setSelectedReport(report);
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setDateRange({ start: '', end: '' });
  };

  const handleDateChange = (event) => {
    setDateRange({ ...dateRange, [event.target.name]: event.target.value });
  };

  const handleSubmitReport = () => {
    // Here you would typically call an API to generate the report

    handleCloseDialog();
    setOpenSnackbar(true);
  };

  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h4" gutterBottom sx={{ mb: 4, fontWeight: 'bold', color: 'primary.main' }}>
        Rapports et Analyses
      </Typography>
      <Grid container spacing={3}>
        {reportTypes.map((report, index) => (
          <Grid item xs={12} sm={6} md={4} key={index}>
            <Card sx={{ 
              height: '100%', 
              display: 'flex', 
              flexDirection: 'column',
              transition: 'transform 0.3s, box-shadow 0.3s',
              '&:hover': {
                transform: 'translateY(-5px)',
                boxShadow: '0 4px 20px rgba(0,0,0,0.1)',
              }
            }}>
              <CardContent sx={{ flexGrow: 1 }}>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                  {report.icon}
                  <Typography variant="h6" component="div" sx={{ ml: 1 }}>
                    {report.title}
                  </Typography>
                </Box>
                <Typography variant="body2" color="text.secondary">
                  {report.description}
                </Typography>
              </CardContent>
              <CardActions>
                <Button 
                  size="small" 
                  onClick={() => handleGenerateReport(report)}
                  sx={{ 
                    backgroundColor: 'primary.main', 
                    color: 'white',
                    '&:hover': {
                      backgroundColor: 'primary.dark',
                    }
                  }}
                >
                  Générer le rapport
                </Button>
              </CardActions>
            </Card>
          </Grid>
        ))}
      </Grid>

      <Dialog open={openDialog} onClose={handleCloseDialog}>
        <DialogTitle>{selectedReport?.title}</DialogTitle>
        <DialogContent>
          <TextField
            label="Date de début"
            type="date"
            name="start"
            value={dateRange.start}
            onChange={handleDateChange}
            fullWidth
            margin="normal"
            InputLabelProps={{
              shrink: true,
            }}
          />
          <TextField
            label="Date de fin"
            type="date"
            name="end"
            value={dateRange.end}
            onChange={handleDateChange}
            fullWidth
            margin="normal"
            InputLabelProps={{
              shrink: true,
            }}
          />
          <TextField
            select
            label="Format du rapport"
            value="pdf"
            fullWidth
            margin="normal"
          >
            <MenuItem value="pdf">PDF</MenuItem>
            <MenuItem value="excel">Excel</MenuItem>
            <MenuItem value="csv">CSV</MenuItem>
          </TextField>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Annuler</Button>
          <Button onClick={handleSubmitReport} variant="contained" color="primary">
            Générer
          </Button>
        </DialogActions>
      </Dialog>

      <Snackbar 
        open={openSnackbar} 
        autoHideDuration={6000} 
        onClose={() => setOpenSnackbar(false)}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
      >
        <Alert onClose={() => setOpenSnackbar(false)} severity="success" sx={{ width: '100%' }}>
          Le rapport a été généré avec succès !
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default ReportsInterface;