import React, { useState, useEffect } from 'react';
import {
  Box,
  Typography,
  Grid,
  Paper,
  Tabs,
  Tab,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Divider,
} from '@mui/material';
import {
  School as SchoolIcon,
  Group as GroupIcon,
} from '@mui/icons-material';
import SessionDetailsModal from '../tuteur/SessionDetailsModal';
import SeanceService from '../../services/seancesService';
import capitalizeString from '../../utils/capitalize';

const Dashboard = ({ user, tuteurId }) => {

  const [activeTab, setActiveTab] = useState(0);
  const [seances, setSeances] = useState([]);

  const [selectedSession, setSelectedSession] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  useEffect(() => {
    const fetchData = async () => {  //getSeancesByTuteurId
      if (tuteurId) {
        const fetchedSeances = await SeanceService.getSeancesByTuteurId(tuteurId);
        setSeances(fetchedSeances);
      }
    };

    fetchData();
  }, [tuteurId]);

  const handleTabChange = (event, newValue) => {
    setActiveTab(newValue);
  };

  const handleViewDetails = async (seanceId) => {

    const seanceDetails = await SeanceService.getSeanceById(seanceId);
    setSelectedSession(seanceDetails);

    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setSelectedSession(null);
  };

  const renderAssignments = () => (
    <TableContainer component={Paper}>
      <Table>
        <TableHead sx={{ background: 'purple', color: 'white' }}>
          <TableRow>
            <TableCell sx={{ color: 'white', fontWeight: 'bold' }}>Module</TableCell>
            <TableCell sx={{ color: 'white', fontWeight: 'bold' }}>Group</TableCell>
            <TableCell sx={{ color: 'white', fontWeight: 'bold' }}>Total Hours</TableCell>
            <TableCell sx={{ color: 'white', fontWeight: 'bold' }}>Completed Hours</TableCell>
            <TableCell sx={{ color: 'white', fontWeight: 'bold' }}>Actions</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {seances ? seances.map((seance) => (
            <TableRow key={seance.id}>
              <TableCell>{capitalizeString(seance.module.nom)}</TableCell>
              <TableCell>{capitalizeString(seance.groupe.nom)}</TableCell>
              <TableCell>{seance.module.nombreSemaines * 2}</TableCell>
              <TableCell>{seance.heuresEffectuees}</TableCell>
              <TableCell>
                <Button variant="contained" size="small" onClick={() => handleViewDetails(seance.id)}>View Details</Button>
              </TableCell>
            </TableRow>
          )) : <TableRow  >
            <TableCell colSpan={5} align="center">
              Aucun résultat trouvé
            </TableCell>
          </TableRow>}
        </TableBody>
      </Table>
    </TableContainer>
  );

  return (
    <Box sx={{ flexGrow: 1, p: 3 }}>
      <Typography variant="h4" gutterBottom color='dark'>
        Bienvenue, {user ? `${capitalizeString(user.prenom)} ${capitalizeString(user.nom)}` : 'N/A'}
      </Typography>
      <Divider sx={{ mb: 3 }} />
      <Grid container spacing={3} sx={{ width: '100%', display: 'flex', justifyContent: 'center' }} width='100%'>
        <Grid item xs={12} md={6} lg={3}>
          <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
            <SchoolIcon sx={{ fontSize: 40, color: 'secondary.main' }} />
            <Typography variant="h6">Modules</Typography>
            <Typography variant="body1">{seances.length || 0}</Typography>
          </Paper>
        </Grid>
        <Grid item xs={12} md={6} lg={3}>
          <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
            <GroupIcon sx={{ fontSize: 40, color: 'success.main' }} />
            <Typography variant="h6">Groups</Typography>
            <Typography variant="body1">{seances.length || 0}</Typography>
          </Paper>
        </Grid>
      </Grid>
      <Box sx={{ mt: 4 }}>
        <Tabs value={activeTab} onChange={handleTabChange}>
          <Tab label="Assignments" icon={<SchoolIcon />} />
        </Tabs>
        <Box sx={{ mt: 2 }}>
          {renderAssignments()}
        </Box>
      </Box>
      <SessionDetailsModal open={isModalOpen} onClose={handleCloseModal} session={selectedSession && selectedSession} />
    </Box>
  );
};

export default Dashboard;