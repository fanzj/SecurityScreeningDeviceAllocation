package com.fzj.alg;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.fzj.alg.thread.AlgThread;
import com.fzj.config.NameSpace;
import com.fzj.model.SSModel;
import com.fzj.utils.MProperties;

public class JUnitTest {
	
	private Properties m_aTC_p = null;
	private int m_aI4_size;
	private int m_aI4_run_times;
	private int m_aI4_max_nfe;
	private String m_str_data_path;
	private SSModel m_aTC_ssm;

	@Before
	public void setUp() throws Exception {
		if(m_aTC_p==null){
			m_aTC_p = MProperties.get(NameSpace.s_str_run_test_config);
		}
		m_aI4_size = Integer.valueOf(m_aTC_p.getProperty(NameSpace.s_str_population_size));
		m_aI4_run_times = Integer.valueOf(m_aTC_p.getProperty(NameSpace.s_str_run_times));
		m_aI4_max_nfe = Integer.valueOf(m_aTC_p.getProperty(NameSpace.s_str_max_nfe));
		m_str_data_path = NameSpace.s_str_data_02;
		m_aTC_ssm = new SSModel(m_str_data_path);
	}
	


	@Test
	public void testDNSPSO() {
	
	}
	
	@Test
	public void testDE_DNSPSO(){
		
	}
	
	@Test
	public void testFADE(){
		
	}
	
	@Test
	public void testWWO(){
		
	}

}
