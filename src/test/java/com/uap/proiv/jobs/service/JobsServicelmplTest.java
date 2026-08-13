package com.uap.proiv.jobs;

import java.util.List;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import com.uap.proiv.jobs.client.JobApiRepository;
import com.uap.proiv.jobs.dto.Job;
import com.uap.proiv.jobs.service.impl.JobServiceImpl;

@ExtendWith(MockitoExtension.class)
public class JobsServicelmplTest {

    @Mock
    JobApiRepository jobApiRepository;

    @InjectMocks
    JobServiceImpl jobServiceImpl;

    List<Job> jobs;

    @BeforeEach
    void setUp() {
        jobs = new ArrayList<>();
        Job job1 = new Job();
        job1.setId(1);
        job1.setName("Developer");
        job1.setSalary(5000);
        job1.setHours(2000);
        job1.setResources(3);
        jobs.add(job1);
    }

    @Test
    @DisplayName("verifica que el metodo getAllJobs() devuelve una lista de trabajos")
    void getAllJobs_Succeeds() {
        when(jobApiRepository.getAllJobs()).thenReturn(jobs);

        List<Job> result = jobServiceImpl.getAllJobs();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Developer", result.get(0).getName());
    }

    @Test
    @DisplayName("verifica una excepcion en jobapirepository")
    void getAllJob_Exception() {
        when(jobApiRepository.getAllJobs()).thenThrow(new RuntimeException("Error en el repositorio"));

        try {
            jobServiceImpl.getAllJobs();
        } catch (RuntimeException e) {
            verify(jobApiRepository, times(1)).getAllJobs();
        }
    }
}