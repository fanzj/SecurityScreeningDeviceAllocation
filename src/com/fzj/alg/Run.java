package com.fzj.alg;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.fzj.alg.thread.AlgThread;
import com.fzj.config.NameSpace;
import com.fzj.utils.MProperties;

public class Run {
	
	private Properties m_aTC_p = null;
	private int m_aI4_size;
	private int m_aI4_run_times;
	private int m_aI4_max_nfe;
	private String m_str_data_path;

	@Before
	public void setUp() throws Exception {
		m_aTC_p = MProperties.get(NameSpace.s_str_run_config);
		m_aI4_size = Integer.valueOf(m_aTC_p.getProperty(NameSpace.s_str_population_size));
		m_aI4_run_times = Integer.valueOf(m_aTC_p.getProperty(NameSpace.s_str_run_times));
		m_aI4_max_nfe = Integer.valueOf(m_aTC_p.getProperty(NameSpace.s_str_max_nfe));
		m_str_data_path = NameSpace.s_str_data_02;
	}

	@Test
	public void testGA() {
		Thread t_aTC_ga_thread = new AlgThread(NameSpace.s_str_ga,m_aI4_size, m_aI4_max_nfe, Integer.valueOf(m_aTC_p.getProperty(NameSpace.s_str_max_iter_ga)), m_aI4_run_times,m_str_data_path);
		t_aTC_ga_thread.start();
	}

}
