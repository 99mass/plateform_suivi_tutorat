import React from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  Typography,
  Grid,
  Divider,
  Box,
  Chip,
  Paper,
  Alert,
} from '@mui/material';
import {
  Person as PersonIcon,
  School as SchoolIcon,
  Group as GroupIcon,
  CheckCircle as CheckCircleIcon,
  Cancel as CancelIcon,
  Pending as PendingIcon,
  Verified,
  Report,
  AccessTime as AccessTimeIcon,
  Pending,
} from '@mui/icons-material';
import { Stack } from '@mui/system';
import { styled } from '@mui/material/styles';
import capitalizeString from '../../utils/capitalize';

const SessionDetailsModal = ({ open, onClose, session }) => {
  if (!session) return null;

  const totalHours = session.module.nombreSemaines * 2;
  const completedHours = session.heuresEffectuees;
  const missedHours = session.heuresNonEffectuees;
  const completionRatio = completedHours / totalHours;

  const getStatus = (ratio) => {
    if (ratio === 1) return 'completed';
    if (ratio >= 0.75) return 'in progress';
    if (ratio > 0) return 'started';
    return 'not started';
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'completed':
        return 'success';
      case 'in progress':
        return 'primary';
      case 'started':
        return 'warning';
      case 'not started':
        return 'error';
      default:
        return 'default';
    }
  };

  const getStatusIcon = (status) => {
    switch (status) {
      case 'completed':
        return <CheckCircleIcon />;
      case 'in progress':
        return <Pending />;
      case 'started':
        return <PendingIcon />;
      case 'not started':
        return <CancelIcon />;
      default:
        return null;
    }
  };

  const getFeedbackMessage = (completionRatio, missedHours) => {
    if (completionRatio === 1) {
      return {
        type: 'success',
        message: 'Excellent travail ! Vous avez complété toutes les heures prévues. Continuez comme ça !',
      };
    } else if (completionRatio >= 0.75) {
      return {
        type: 'info',
        message: 'Bon progrès ! Vous êtes sur la bonne voie. Continuez vos efforts pour atteindre tous vos objectifs.',
      };
    } else if (completionRatio >= 0.5) {
      return {
        type: 'warning',
        message: 'Vous avez fait du bon travail jusqu\'à présent, mais il reste encore du chemin à parcourir. Essayez d\'augmenter votre assiduité.',
      };
    } else {
      return {
        type: 'error',
        message: `Attention ! Vous avez manqué ${missedHours} heures. Il est important d'améliorer votre assiduité pour atteindre vos objectifs.`,
      };
    }
  };

  const status = getStatus(completionRatio);
  const feedback = getFeedbackMessage(completionRatio, missedHours);

  const DemoPaper = styled(Paper)(({ theme }) => ({
    width: 120,
    height: '100%',
    padding: theme.spacing(2),
    ...theme.typography.body2,
    textAlign: 'center',
    background: '#e7e7e7',
    color:'black'
  }));

  return (
    <Dialog open={open} onClose={onClose} maxWidth="md" fullWidth>
      <DialogTitle>
        Séance
        <Chip
          label={status}
          color={getStatusColor(status)}
          icon={getStatusIcon(status)}
          sx={{ ml: 2 }}
        />
      </DialogTitle>
      <Divider />
      <DialogContent>
        <Grid container spacing={3}>
          <Grid item xs={12} md={6}>
            <Box display="flex" alignItems="center" mb={2}>
              <SchoolIcon color="primary" sx={{ mr: 1, fontSize: 35 }} />
              <Typography variant="h6">Module: {capitalizeString(session.module.nom)  || 'N/A'}</Typography>
            </Box>
            <Box display="flex" alignItems="center" mb={2}>
              <GroupIcon color="primary" sx={{ mr: 1, fontSize: 35 }} />
              <Typography variant="h6">Group: {capitalizeString(session.groupe.nom)  || 'N/A'}</Typography>
            </Box>
          </Grid>
          <Grid item xs={12} md={6} sx={{ borderLeft: '0.5px solid gray' }}>
            <Box display="flex" alignItems="center" >
              <PersonIcon color="primary" sx={{ mr: 1 ,fontSize: 35}} />
              <Typography variant="h6">Tuteur: {`${capitalizeString(session.tuteur.prenom)} ${capitalizeString(session.tuteur.nom)}` || 'N/A'}</Typography>
            </Box>
            <Divider sx={{ my: 3 }} />
            <Box sx={{ mb: 2 }}>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                <AccessTimeIcon color='primary' sx={{ fontSize: 30, mr: 1, }} />
                <Typography variant="body1" color="text.secondary">
                  Semaines: {session.module.nombreSemaines} | Total: {totalHours}h
                </Typography>
              </Box>
              <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                <Box display="flex" alignItems="center" >
                  <Verified color="success.main" sx={{ mr: 0.5, fontSize: 30, color: 'green' }} />
                  <Typography variant="body1" color="success.main">
                    H. Effectuées: {completedHours}h
                  </Typography>
                </Box>
                <Box display="flex" alignItems="center" >
                  <Report color="error.main" sx={{ mr: 0.5, fontSize: 30, color: 'red' }} />
                  <Typography variant="body1" color="error.main">
                    H. Non Effectuées: {missedHours}h
                  </Typography>
                </Box>
              </Box>
            </Box>
          </Grid>
        </Grid>
        <Divider sx={{ my: 2 }} />
        <Box mb={2}>
          <Alert severity={feedback.type} variant="outlined">
            {feedback.message}
          </Alert>
        </Box>
        <Divider sx={{ mb: 2 }} />
        <Box>
          <Typography variant="h6">Dates seances </Typography>
          <Stack direction="row" spacing={2}>
            {session?.dates && session.dates.map((date, index) => (
              <DemoPaper key={index} variant="elevation">{date}</DemoPaper>
            ))}
          </Stack>
        </Box>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} color="primary">
          Close
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default SessionDetailsModal;