package com.uap.proiv.jobs.service;

import java.util.List;

import com.uap.proiv.jobs.dto.Job;
import com.uap.proiv.jobs.dto.JobRequest;

public interface JobService {
    List<Job> getAllJobs();
    Job getJobById(int id);
    Job add(JobRequest request);
}
