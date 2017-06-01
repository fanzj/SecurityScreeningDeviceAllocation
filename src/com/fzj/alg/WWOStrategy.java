package com.fzj.alg;

import java.util.Arrays;

import com.fzj.compare.FitnessComparator;
import com.fzj.config.NameSpace;
import com.fzj.model.SSModel;
import com.fzj.solution.ASolution;
import com.fzj.solution.WaveSolution;
import com.fzj.utils.FileUtils;
import com.fzj.utils.MathUtils;

/**
 * @author Fan Zhengjie
 * @date 2017��2��11�� ����2:17:28
 * @version 1.0
 * @description
 */
public class WWOStrategy extends AStrategy {

	private double m_aI8_epsilon = 0.0001;
	private int m_aI4_Hmax;// ��󲨸�
	private double m_aI8_a;// ��������ϵ��
	private double m_aI8_b;// ����ϵ��
	private double m_aI8_bmax;
	private double m_aI8_bmin;
	private int m_aI4_nmax;
	private int m_aI4_nmin;
	private int m_aI4_Kmax;
	public WaveSolution m_aTC_best;// ��ǰ��ú����Ľ�
	private WaveSolution m_aTC_worst;
	private double m_rI8_range[];// ��dά�����ռ�ĳ��ȣ��˴�һ��
	private int m_aI4_xmax;//λ������
	private int m_aI4_xmin;
	private int m_aI4_ymax;//λ������
	private int m_aI4_ymin;

	public WWOStrategy() {
		super();
	}

	public WWOStrategy(int f_aI4_size, int f_aI4_max_nfe, int f_aI4_max_iter, SSModel f_aTC_ssm,
			String f_str_data_path, long f_aI8_max_time) {
		super(f_aI4_size, f_aI4_max_nfe, f_aI4_max_iter, f_aTC_ssm, f_str_data_path, f_aI8_max_time);
	}

	@Override
	protected void init() {
		this.m_aI4_nmax = m_aI4_size;
		this.m_aI4_nmin = 3;

		this.m_aI4_Hmax = 12;
		this.m_aI8_a = 1.0026;
		this.m_aI8_bmax = 0.25;
		this.m_aI8_bmin = 0.001;
		this.m_aI8_b = m_aI8_bmax;

		this.m_aI4_xmax = m_aI4_k;
		this.m_aI4_xmin = 1;
		this.m_aI4_ymax = m_aI4_q;
		this.m_aI4_ymin = 0;
		
		m_aTC_population = new WaveSolution[m_aI4_size];
		m_aI4_Kmax = Math.min(12, m_aI4_d / 2);

		m_rI8_range = new double[m_aI4_d];
		for (int t_aI4_i = 0; t_aI4_i < m_aI4_d; t_aI4_i++) {
			m_rI8_range[t_aI4_i] = t_aI4_i%2==0?(m_aI4_k-1):(m_aI4_q);
		}

		m_aTC_best = new WaveSolution(m_aI4_d);
		m_aTC_worst = new WaveSolution(m_aI4_d);
		m_aTC_best.setM_aI8_fitness(Double.MIN_VALUE);
		m_aTC_worst.setM_aI8_fitness(Double.MAX_VALUE);

		m_str_alg_name = NameSpace.s_str_wwo;
		m_str_file_name = FileUtils.getResultName(m_str_alg_name, NameSpace.s_str_file_txt, m_aI4_max_nfe);
	}
	
	private void updateBestAndWorst(){
		for(int t_aI4_i=0;t_aI4_i<m_aI4_size;t_aI4_i++){
			WaveSolution t_aTC_wave = (WaveSolution) m_aTC_population[t_aI4_i];
			if(Double.compare(m_aTC_best.getM_aI8_fitness(), t_aTC_wave.getM_aI8_fitness())<0)
				m_aTC_best = t_aTC_wave.clone();
			if(Double.compare(m_aTC_worst.getM_aI8_fitness(), t_aTC_wave.getM_aI8_fitness())>0)
				m_aTC_worst = t_aTC_wave.clone();
		}
	}
	
	/**
	 * ����Ⱥ����Ӧ�ȴӴ�С����
	 */
	private void sortPopulation(){
		Arrays.sort(m_aTC_population, new FitnessComparator());
	}
	
	/**
	 * ������Ⱥ��ģ
	 */
	private void updateN(){
		double t_aI8_temp = (m_aI4_nmax-m_aI4_nmin)*((double)m_aI4_cur_iter/(double)m_aI4_max_iter);
		m_aI4_size = m_aI4_nmax - (int)Math.round(t_aI8_temp);
	}
	
	/**
	 * ��������ϵ��
	 */
	private void updateB(){
		m_aI8_b = m_aI8_bmax - (m_aI8_bmax-m_aI8_bmin) * m_aI4_cur_iter/(double)m_aI4_max_iter;
	}
	
	/**
	 * ���²���
	 */
	private void updateW() {
		for (int t_aI4_i = 0; t_aI4_i < m_aI4_size;t_aI4_i++) {
			WaveSolution t_aTC_wave = (WaveSolution) m_aTC_population[t_aI4_i];
			double t_aI8_w = t_aTC_wave.getM_aI8_w();// ����
			t_aI8_w *= Math.pow(m_aI8_a,
					-(t_aTC_wave.getM_aI8_fitness() - m_aTC_worst.getM_aI8_fitness() + m_aI8_epsilon)
					/ (m_aTC_best.getM_aI8_fitness() - m_aTC_worst.getM_aI8_fitness() + m_aI8_epsilon));
			t_aTC_wave.setM_aI8_w(t_aI8_w);
		}
	}

	

	/**
	 * ����
	 * 
	 * @param f_aTC_wave
	 */
	private void propagate(WaveSolution f_aTC_wave) {
		double t_aI8_w = f_aTC_wave.getM_aI8_w();
		int[] t_rI4_x = f_aTC_wave.getM_rI4_x();
		for(int t_aI4_j=0;t_aI4_j<m_aI4_d;t_aI4_j++){
			int t_aI4_xd = t_rI4_x[t_aI4_j];
			double t_aI8_r = MathUtils.getDoubleAToB(-1, 1);
			int t_aI8_y = (int) Math.round(t_aI4_xd+t_aI8_r*t_aI8_w*m_rI8_range[t_aI4_j]);
			//System.out.println("t_aI8_y = "+t_aI8_y);
		
			if(t_aI4_j%2==0){
				if(t_aI8_y > m_aI4_xmax){
					t_aI8_y = m_aI4_xmax;
				}else if(t_aI8_y < m_aI4_xmin){
					t_aI8_y = m_aI4_xmin;
				}
			}else {
				if(t_aI8_y > m_aI4_ymax){
					t_aI8_y = m_aI4_ymax;
				}else if(t_aI8_y < m_aI4_ymin){
					t_aI8_y = m_aI4_ymin;
				}
			}
			t_rI4_x[t_aI4_j] = t_aI8_y;
		}
		f_aTC_wave.setM_rI4_x(t_rI4_x);
		modify(f_aTC_wave);
		evaluate(f_aTC_wave);
		saveFitness(m_aTC_best);
		m_aI4_cur_nfe++;
	}



	/**
	 * ����
	 * 
	 * @param f_aTC_wave
	 */
	private WaveSolution refract(WaveSolution f_aTC_wave) {
		WaveSolution t_aTC_wave2 = f_aTC_wave.clone();
		int[] t_rI4_x2 = t_aTC_wave2.getM_rI4_x();
		int[] t_rI4_xb = m_aTC_best.getM_rI4_x();
		
		for(int t_aI4_j=0;t_aI4_j<m_aI4_d;t_aI4_j++){
			//��˹�ֲ�N(u,g)
			double t_aI8_u = (t_rI4_x2[t_aI4_j]+t_rI4_xb[t_aI4_j])/2.0;
			double t_aI8_g = Math.abs(t_rI4_xb[t_aI4_j]-t_rI4_x2[t_aI4_j])/2.0;
			double t_aI8_r = Math.sqrt(t_aI8_g)*m_aTC_random.nextGaussian()+t_aI8_u;
			int t_aI4_temp = new Double(Math.round(t_aI8_r)).intValue();
			//System.out.println("t_aI4_temp = "+t_aI4_temp);
			if(t_aI4_j%2==0){
				if(t_aI4_temp > m_aI4_xmax){
					t_aI4_temp = m_aI4_xmax;
				}else if(t_aI4_temp < m_aI4_xmin){
					t_aI4_temp = m_aI4_xmin;
				}
			}else {
				if(t_aI4_temp > m_aI4_ymax){
					t_aI4_temp = m_aI4_ymax;
				}else if(t_aI4_temp < m_aI4_ymin){
					t_aI4_temp = m_aI4_ymin;
				}
			}
			t_rI4_x2[t_aI4_j] = t_aI4_temp;
		}
		t_aTC_wave2.setM_rI4_x(t_rI4_x2);
		modify(t_aTC_wave2);
		evaluate(t_aTC_wave2);
		saveFitness(m_aTC_best);
		t_aTC_wave2.setM_aI4_h(m_aI4_Hmax);
		m_aI4_cur_nfe++;
		
		double t_aI8_r = f_aTC_wave.getM_aI8_fitness() / t_aTC_wave2.getM_aI8_fitness();
		if(Double.compare(t_aI8_r, 1)<0){//����󣬲�����С��˵������ţ�����
			double t_aI8_w = f_aTC_wave.getM_aI8_w() * t_aI8_r;
			t_aTC_wave2.setM_aI8_w(t_aI8_w);
			f_aTC_wave = t_aTC_wave2;
			return t_aTC_wave2;
		}else {//����ԭ�н⣬��������
			return f_aTC_wave;
		}
		
	}

	/**
	 * ����
	 * 
	 * @param f_aTC_wave
	 */
	private void breaking(WaveSolution f_aTC_wave) {
		WaveSolution t_aTC_bestWave = new WaveSolution(m_aI4_d);
		t_aTC_bestWave.setM_aI8_fitness(Double.MIN_VALUE);
		int t_aI4_k = 1+m_aTC_random.nextInt(m_aI4_Kmax);
		for(int t_aI4_i=0;t_aI4_i<t_aI4_k;t_aI4_i++){
			WaveSolution t_aTC_newWave = f_aTC_wave.clone();
			int t_aI4_d = m_aTC_random.nextInt(m_aI4_d);
			int[] t_rI4_x = t_aTC_newWave.getM_rI4_x();
			double t_aI8_y = t_rI4_x[t_aI4_d]+m_aTC_random.nextGaussian()*m_aI8_b*m_rI8_range[t_aI4_d];
			int t_aI4_temp = new Double(Math.round(t_aI8_y)).intValue();
			//System.out.println("breaking = "+t_aI4_temp);
	
			if(t_aI4_d%2==0){
				if(t_aI4_temp > m_aI4_xmax){
					t_aI4_temp = m_aI4_xmax;
				}else if(t_aI4_temp < m_aI4_xmin){
					t_aI4_temp = m_aI4_xmin;
				}
			}else {
				if(t_aI4_temp > m_aI4_ymax){
					t_aI4_temp = m_aI4_ymax;
				}else if(t_aI4_temp < m_aI4_ymin){
					t_aI4_temp = m_aI4_ymin;
				}
			}
			t_rI4_x[t_aI4_d] = t_aI4_temp;
			t_aTC_newWave.setM_rI4_x(t_rI4_x);
			modify(t_aTC_newWave);
			evaluate(t_aTC_newWave);
			saveFitness(m_aTC_best);
			m_aI4_cur_nfe++;
			
			if(Double.compare(t_aTC_newWave.getM_aI8_fitness(), t_aTC_bestWave.getM_aI8_fitness())>0){
				t_aTC_bestWave = t_aTC_newWave;
			}
		}
		
		//���ŵĶ����������ŵĲ���
		if(Double.compare(t_aTC_bestWave.getM_aI8_fitness(), m_aTC_best.getM_aI8_fitness())>0){
			m_aTC_best = t_aTC_bestWave;
		}
		
	}
	
	/**
	 * ����
	 */
	private void evolve() {
		for (int t_aI4_i = 0; t_aI4_i < m_aI4_size; t_aI4_i++) {
			WaveSolution t_aTC_wave = (WaveSolution) m_aTC_population[t_aI4_i];//�����������޸�
			WaveSolution t_aTC_temp = t_aTC_wave.clone();//���룬��Ϊ����
			
			propagate(t_aTC_temp);//����,t_aTC_temp��Ϊ���������ɵ��²�
			if(Double.compare(t_aTC_temp.getM_aI8_fitness(), t_aTC_wave.getM_aI8_fitness())>0){
				t_aTC_temp.setM_aI4_h(m_aI4_Hmax);
				if(Double.compare(t_aTC_temp.getM_aI8_fitness(), m_aTC_best.getM_aI8_fitness())>0){
					//����
					m_aTC_best = t_aTC_temp.clone();
					breaking(t_aTC_temp);
				}
				t_aTC_wave = t_aTC_temp;
			}else {
				int t_aI4_h = t_aTC_wave.getM_aI4_h();
				t_aI4_h--;
				t_aTC_wave.setM_aI4_h(t_aI4_h);
				if(t_aI4_h==0){
					t_aTC_wave = refract(t_aTC_wave);//����
				}
			}
			m_aTC_population[t_aI4_i] = t_aTC_wave.clone();
			
		}
	}


	@Override
	protected void evolution() {
		long t_aI8_start = System.currentTimeMillis();
		while(m_aI4_cur_nfe<=m_aI4_max_nfe) {
			evolve();
			sortPopulation();
			updateW();
			updateBestAndWorst();
			updateB();
			//updateN();
			m_aI4_cur_iter++;
		}
		
		System.out.println(m_str_alg_name+" m_aI8_time = "+(System.currentTimeMillis() - t_aI8_start)/1000.0);
		System.out.println(m_str_alg_name+" m_aI4_cur_nfe = "+m_aI4_cur_nfe);
		System.out.println(m_str_alg_name+" m_aI4_cur_iter = "+m_aI4_cur_iter);
	}
	
	/**
	 * ��ʼ����Ⱥ�ĺ�������
	 */
	private void initPopulation2(){
		for(int t_aI4_i=0;t_aI4_i<m_aI4_size;t_aI4_i++){
			WaveSolution t_aTC_wave = (WaveSolution) m_aTC_population[t_aI4_i];
			t_aTC_wave.setM_aI4_h(m_aI4_Hmax);
			t_aTC_wave.setM_aI8_w(0.5);
		}
	}

	@Override
	protected ASolution solve(int f_aI4_p) {
		// ��ʼ��
		{
			init();
			initPopulation(NameSpace.s_str_wwo);
			initPopulation2();
			updateBestAndWorst();
		}

		// ��ӡ���������ڲ��Ե���
		{

			/*System.out.println("==========��ʼ����Ⱥ==========");
		    printPopulation(m_aTC_population);
			System.out.println("==========��Ⱥ������==========");
			printSolution(m_aTC_best);
			System.out.println("==========��Ⱥ�����==========");
			printSolution(m_aTC_worst);*/

		}

		// ��Ⱥ����
		{
			evolution();
		}

		// ����ı���
		{
			String t_str_result_content = getResultContent(f_aI4_p,m_aTC_best);
			FileUtils.saveFile(m_str_data_path, m_str_alg_name,m_str_file_name, t_str_result_content);
		}

		return m_aTC_best;
	}
	
	public static void main(String[] args) {
		SSModel t_aTC_ssm = new SSModel(NameSpace.s_str_data_01);
		AStrategy t_aTC_strategy = new WWOStrategy(10, 1000, 500, t_aTC_ssm, NameSpace.s_str_data_01,1000);
		t_aTC_strategy.solve(0);
	}

}
