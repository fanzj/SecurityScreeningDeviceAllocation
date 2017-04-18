package com.fzj.alg;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import com.fzj.config.NameSpace;
import com.fzj.model.SSModel;
import com.fzj.solution.ASolution;
import com.fzj.solution.ParticleSolution;
import com.fzj.utils.FileUtils;

/** 
 * @author Fan Zhengjie 
 * @date 2017��2��3�� ����7:14:57 
 * @version 1.0 
 * @description DNSPSO�㷨
 */
public class DNSPSOStrategy extends AStrategy{
	
	private double m_aI8_c1;//ѧϰ����
	private double m_aI8_c2;//ѧϰ����
	private int m_aI4_vmax;//����ٶ�
	private int m_aI4_vmin;
	private int m_aI4_xmax;//λ������
	private int m_aI4_xmin;
	private double m_aI8_w;//����Ȩ��
	private double m_aI8_wmin;//��СȨ��ϵ��
	private double m_aI8_wmax;//���Ȩ��ϵ��
	public ParticleSolution m_aTC_gBest;//ȫ����������
	private ParticleSolution[] m_aTC_pBest;//��ʷ��������
	
	//DNSPSO���PSO���ӵĲ���
	private double m_aI8_pr;//���ڶ��������ӻ���
	private double m_aI8_pns;//����������������
	private int m_aI4_K;
	private double m_aI8_r1,m_aI8_r2,m_aI8_r3,m_aI8_r4,m_aI8_r5,m_aI8_r6;//���������������ɵ������
	
	private ReentrantLock m_aTC_lock;
	
    public DNSPSOStrategy() {
    	super();
	}

    public DNSPSOStrategy(int f_aI4_size,int f_aI4_max_nfe,int f_aI4_max_iter,SSModel f_aTC_ssm,String f_str_data_path){
		super(f_aI4_size, f_aI4_max_nfe, f_aI4_max_iter,f_aTC_ssm,f_str_data_path);
	}
    
	@Override
	protected void init() {
		this.m_aI8_c1 = 1.49618;
		this.m_aI8_c2 = 1.49618;
		this.m_aI8_wmin = 0.4;
		this.m_aI8_wmax = 0.9;
		this.m_aI8_pr = 0.9;
		this.m_aI8_pns = 0.6;
		this.m_aI4_K = 2;
		
		this.m_aI8_w = m_aI8_wmax;
		this.m_aTC_population = new ParticleSolution[m_aI4_size];
		
		this.m_aI4_vmax = 1;
		this.m_aI4_vmin = 0;
		this.m_aI4_xmax = 1;
		this.m_aI4_xmin = 0;
	
		
		m_str_alg_name = NameSpace.s_str_dnspso;
		m_str_file_name = FileUtils.getResultName(m_str_alg_name, NameSpace.s_str_file_txt,m_aI4_max_nfe);
		
		m_aTC_lock = new ReentrantLock();
	}
	
	/**
	 * ����Ȩ�صĸ���
	 */
	private void updateW(){
		m_aI8_w = m_aI8_wmax - ((double) m_aI4_cur_iter / (double) m_aI4_max_iter) * (m_aI8_wmax - m_aI8_wmin);
	}
	
	/**
	 * ��ʼ����Ⱥ��λ�Ƶĳ�ʼ��
	 */
	private void initV(){
		for(int t_aI4_k=0;t_aI4_k<m_aI4_size;t_aI4_k++){
			ParticleSolution t_aTC_particle = (ParticleSolution) m_aTC_population[t_aI4_k];
			int[] t_aI4_x = t_aTC_particle.getM_rI4_x();
			t_aTC_particle.setM_rI4_v(t_aI4_x);
		}
	}
	/**
	 * ȫ�����ź���ʷ�������ӵĸ���
	 */
	private void initBest(){
		this.m_aTC_pBest = new ParticleSolution[m_aI4_size];
		this.m_aTC_gBest = new ParticleSolution(m_aI4_d);
		m_aTC_gBest.setM_aI8_fitness(Double.MIN_VALUE);
		
		for(int t_aI4_k=0;t_aI4_k<m_aI4_size;t_aI4_k++){
			m_aTC_pBest[t_aI4_k] = ((ParticleSolution) (m_aTC_population[t_aI4_k])).clone();
			if(Double.compare(m_aTC_gBest.getM_aI8_fitness(), m_aTC_pBest[t_aI4_k].getM_aI8_fitness())<0){
				m_aTC_gBest = (ParticleSolution) m_aTC_pBest[t_aI4_k].clone();
			}
		}
	}
	
	/**
	 * ����pBest��gBest
	 * @param f_aI4_k
	 */
	private void updateBest(int f_aI4_k){
		if(Double.compare(m_aTC_pBest[f_aI4_k].getM_aI8_fitness(), m_aTC_population[f_aI4_k].getM_aI8_fitness())<0){
			m_aTC_pBest[f_aI4_k] = ((ParticleSolution) m_aTC_population[f_aI4_k]).clone();
		}
		if(Double.compare(m_aTC_gBest.getM_aI8_fitness(), m_aTC_pBest[f_aI4_k].getM_aI8_fitness())<0){
			m_aTC_gBest = m_aTC_pBest[f_aI4_k].clone();
		}
	}
	
	/**
	 * �����ٶ�λ��
	 * @param f_aTC_solution
	 * @param f_aI4_index
	 */
	private void updateXV(ParticleSolution f_aTC_solution,int f_aI4_index){
		double t_aI8_r1,t_aI8_r2;
		int[] t_rI4_xp = m_aTC_pBest[f_aI4_index].getM_rI4_x();
		int[] t_rI4_xg = m_aTC_gBest.getM_rI4_x();
		int[] t_rI4_cur_x = f_aTC_solution.getM_rI4_x();
		int[] t_rI4_cur_v = f_aTC_solution.getM_rI4_v();
		int[] t_rI4_new_x = new int[m_aI4_d];
		int[] t_rI4_new_v = new int[m_aI4_d];
		for(int t_aI4_j=0;t_aI4_j<m_aI4_d;t_aI4_j++){
			t_aI8_r1 = m_aTC_random.nextDouble();
			t_aI8_r2 = m_aTC_random.nextDouble();
			double t_aI8_temp_v = m_aI8_w * t_rI4_cur_v[t_aI4_j] + m_aI8_c1 * t_aI8_r1 * (t_rI4_xp[t_aI4_j] - t_rI4_cur_x[t_aI4_j]) 
					+ m_aI8_c2 * t_aI8_r2 * (t_rI4_xg[t_aI4_j] - t_rI4_cur_x[t_aI4_j]);
			t_rI4_new_v[t_aI4_j] = new Double(Math.round(t_aI8_temp_v)).intValue();
			//System.out.println("t_rI4_new_v = "+t_rI4_new_v[t_aI4_j]);
			if(t_rI4_new_v[t_aI4_j] > m_aI4_vmax){
				t_rI4_new_v[t_aI4_j] = m_aI4_vmax;
			}
			else if(t_rI4_new_v[t_aI4_j] < m_aI4_vmin){
				t_rI4_new_v[t_aI4_j] = m_aI4_vmin;
			}
			t_rI4_new_x[t_aI4_j] = t_rI4_cur_x[t_aI4_j] + t_rI4_new_v[t_aI4_j];
			if(t_rI4_new_x[t_aI4_j] > m_aI4_xmax){
				t_rI4_new_x[t_aI4_j] = m_aI4_xmax;
			}else if(t_rI4_new_x[t_aI4_j] < m_aI4_xmin){
				t_rI4_new_x[t_aI4_j] = m_aI4_xmin;
			}
			//System.out.println("t_rI4_new_x = "+t_rI4_new_x[t_aI4_j]);
		}
		f_aTC_solution.setM_rI4_x(t_rI4_new_x);
		f_aTC_solution.setM_rI4_v(t_rI4_new_v);
		f_aTC_solution.setM_cur_iter(m_aI4_cur_iter);
		f_aTC_solution.setM_cur_nfe(m_aI4_cur_nfe);
	}
	

	@Override
	protected void evolution() {
		while(m_aI4_cur_nfe < m_aI4_max_nfe){
			//��һ�׶�
			{
				//���۴���m_aI4_size * 2
				for(int t_aI4_i=0;t_aI4_i<m_aI4_size;t_aI4_i++){
					ParticleSolution t_aTC_pit = (ParticleSolution) m_aTC_population[t_aI4_i];
					//��������Pi(t)
					updateXV(t_aTC_pit, t_aI4_i);//��������Pi���ٶȺ�λ��
					modify(t_aTC_pit);
					//��������Pi(t+1)
					ParticleSolution t_aTC_pit2 = t_aTC_pit.clone();
					updateXV(t_aTC_pit2, t_aI4_i);
					modify(t_aTC_pit2);
					evaluate(t_aTC_pit2);
					saveFitness(m_aTC_gBest);
					m_aI4_cur_nfe++;
					//��ʼ��������߻���
					ParticleSolution t_aTC_tpi2 = dem(t_aTC_pit, t_aTC_pit2);//���۴�������
					//��t_aTC_pit2��t_aTC_tpi2��ѡ��Ϻõ�һ����Ϊt_aTC_pit2
					if(Double.compare(t_aTC_tpi2.getM_aI8_fitness(), t_aTC_pit2.getM_aI8_fitness())>0){
						t_aTC_pit2 = t_aTC_tpi2;
					}
					m_aTC_population[t_aI4_i] = t_aTC_pit2;//����
					updateBest(t_aI4_i);
				}
			}
			
			//�ڶ��׶�
			{
				//���۴���m_aI4_size * 2
				randomR();
				for(int t_aI4_i=0;t_aI4_i<m_aI4_size;t_aI4_i++){
					double t_aI8_randi = m_aTC_random.nextDouble();
					if(Double.compare(t_aI8_randi, m_aI8_pns)<=0){
						ParticleSolution t_aTC_Pi = (ParticleSolution) m_aTC_population[t_aI4_i];
						ParticleSolution t_aTC_Li = LNS(t_aTC_Pi,t_aI4_i);//�ֲ���������
					//	System.out.println("--LNS--"+t_aI4_i);
						modify(t_aTC_Li);
					//	System.out.println("--LNS modify--"+t_aI4_i);
						evaluate(t_aTC_Li);
						saveFitness(m_aTC_gBest);
						m_aI4_cur_nfe++;
						//System.out.println("--LNS evaluate--"+t_aI4_i);
						ParticleSolution t_aTC_Gi = GNS(t_aTC_Pi,t_aI4_i);//ȫ����������
					//	System.out.println("--GNS--"+t_aI4_i);
						modify(t_aTC_Gi);
						//System.out.println("--GNS modify--"+t_aI4_i);
						evaluate(t_aTC_Gi);
						saveFitness(m_aTC_gBest);
						m_aI4_cur_nfe++;
						//System.out.println("--GNS evaluate--"+t_aI4_i);
						m_aTC_population[t_aI4_i] = selectFittest(t_aTC_Pi, t_aTC_Li, t_aTC_Gi);
					}
					updateBest(t_aI4_i);
				}
			}
			
			updateW();
			m_aI4_cur_iter++;
//			System.out.println(m_str_alg_name+" m_aI4_cur_nfe = "+m_aI4_cur_nfe);
//			System.out.println(m_str_alg_name+" m_aI4_cur_iter = "+m_aI4_cur_iter);
		}
		System.out.println(m_str_alg_name+" m_aI4_cur_nfe = "+m_aI4_cur_nfe);
		System.out.println(m_str_alg_name+" m_aI4_cur_iter = "+m_aI4_cur_iter);
	}
	
	/**
	 * ��������߻���
	 * @param f_aTC_pit
	 * @param f_aTC_pit2
	 * @return
	 */
	private ParticleSolution dem(ParticleSolution f_aTC_pit,ParticleSolution f_aTC_pit2){
		//�����µ�����TPi2
		ParticleSolution t_aTC_TPi2 = f_aTC_pit2.clone();
		int[] t_rI4_txi2 = new int[m_aI4_d];
		int[] t_rI4_tvi2 = new int[m_aI4_d];
		
		int[] t_rI4_xit = f_aTC_pit.getM_rI4_x();
		int[] t_rI4_xit2 = f_aTC_pit2.getM_rI4_x();
		int[] t_rI4_vit2 = f_aTC_pit2.getM_rI4_v();
		
		double t_aI8_randj = 0;
		for(int t_aI4_j=0;t_aI4_j<m_aI4_d;t_aI4_j++){
			t_aI8_randj = m_aTC_random.nextDouble();
			if(Double.compare(t_aI8_randj, m_aI8_pr)<0){
				t_rI4_txi2[t_aI4_j] = t_rI4_xit2[t_aI4_j];
			}else {
				t_rI4_txi2[t_aI4_j] = t_rI4_xit[t_aI4_j];
			}
			t_rI4_tvi2[t_aI4_j] = t_rI4_vit2[t_aI4_j];
		}
		t_aTC_TPi2.setM_rI4_x(t_rI4_txi2);
		t_aTC_TPi2.setM_rI4_v(t_rI4_tvi2);
		modify(t_aTC_TPi2);
		evaluate(t_aTC_TPi2);
		saveFitness(m_aTC_gBest);
		m_aI4_cur_nfe++;
		return t_aTC_TPi2;
	}
	
	/**
	 * ÿ���������ϵ��
	 */
	private void randomR(){
		m_aI8_r1 = m_aTC_random.nextDouble();
		m_aI8_r2 = m_aTC_random.nextDouble();
		while(Double.compare(m_aI8_r1+m_aI8_r2, 1)>0){
			m_aI8_r2 = m_aTC_random.nextDouble();
		}
		m_aI8_r3 = 1 - m_aI8_r1 - m_aI8_r2;
		m_aI8_r4 = m_aTC_random.nextDouble();
		m_aI8_r5 = m_aTC_random.nextDouble();
		while(Double.compare(m_aI8_r4+m_aI8_r5, 1)>0){
			m_aI8_r5 = m_aTC_random.nextDouble();
		}
		m_aI8_r6 = 1 - m_aI8_r4 - m_aI8_r5;
	}
	
	/**
	 * �ֲ���������
	 * ����Li
	 * @param f_aTC_Pi
	 * @param f_aI4_index
	 * @return
	 */
	private ParticleSolution LNS(ParticleSolution f_aTC_Pi,int f_aI4_index){
		ParticleSolution t_aTC_Li = f_aTC_Pi.clone();
		int[] t_rI4_lxi = new int[m_aI4_d];
		int[] t_rI4_lvi = new int[m_aI4_d];
		int[] t_rI4_pbestxi = m_aTC_pBest[f_aI4_index].getM_rI4_x();
		int[] t_rI4_xi = f_aTC_Pi.getM_rI4_x();
		int[] t_rI4_vi = f_aTC_Pi.getM_rI4_v();
		
		List<Integer> t_aTC_list = new ArrayList<Integer>();
		for(int t_aI4_i=1;t_aI4_i<=m_aI4_K;t_aI4_i++){
			t_aTC_list.add((f_aI4_index+t_aI4_i)%m_aI4_size);
			t_aTC_list.add((f_aI4_index-t_aI4_i+m_aI4_size)%m_aI4_size);
		}
		
		int t_aI4_x = m_aTC_random.nextInt(t_aTC_list.size());
		int t_aI4_y = m_aTC_random.nextInt(t_aTC_list.size());
		while(t_aI4_x==t_aI4_y){
			t_aI4_y = m_aTC_random.nextInt(t_aTC_list.size());
		}
		int t_aI4_c = t_aTC_list.get(t_aI4_x);
		int t_aI4_d = t_aTC_list.get(t_aI4_y);
		ParticleSolution t_aTC_Pc = (ParticleSolution) m_aTC_population[t_aI4_c];
		int[] t_rI4_xc = t_aTC_Pc.getM_rI4_x();
		ParticleSolution t_aTC_Pd = (ParticleSolution) m_aTC_population[t_aI4_d];
		int[] t_rI4_xd = t_aTC_Pd.getM_rI4_x();
		
		for(int t_aI4_j=0;t_aI4_j<m_aI4_d;t_aI4_j++){
			double t_aI8_temp_x = m_aI8_r1 * t_rI4_xi[t_aI4_j] + m_aI8_r2 * t_rI4_pbestxi[t_aI4_j] + m_aI8_r3 * (t_rI4_xc[t_aI4_j]-t_rI4_xd[t_aI4_j]);
			t_rI4_lxi[t_aI4_j] = new Double(Math.round(t_aI8_temp_x)).intValue();
			t_rI4_lvi[t_aI4_j] = t_rI4_vi[t_aI4_j];
//			System.out.println("t_aI8_temp_x = "+t_aI8_temp_x);
//			System.out.println("t_rI4_lxi = "+t_rI4_lxi[t_aI4_j]);
			if(t_rI4_lxi[t_aI4_j] > m_aI4_xmax){
				t_rI4_lxi[t_aI4_j] = m_aI4_xmax;
			}else if(t_rI4_lxi[t_aI4_j] < m_aI4_xmin){
				t_rI4_lxi[t_aI4_j] = m_aI4_xmin;
			}
			if(t_rI4_lvi[t_aI4_j] > m_aI4_vmax){
				t_rI4_lvi[t_aI4_j] = m_aI4_vmax;
			}else if(t_rI4_lvi[t_aI4_j] < m_aI4_vmin){
				t_rI4_lvi[t_aI4_j] = m_aI4_vmin;
			}
		}
		t_aTC_Li.setM_rI4_x(t_rI4_lxi);
		t_aTC_Li.setM_rI4_v(t_rI4_lvi);
		
		return t_aTC_Li;
	}
	
	/**
	 * ȫ���������ز���
	 * @param f_aTC_Pi
	 * @param f_aI4_index
	 * @return
	 */
	private ParticleSolution GNS(ParticleSolution f_aTC_Pi,int f_aI4_index){
		ParticleSolution t_aTC_Gi = f_aTC_Pi.clone();
		int[] t_rI4_gxi = new int[m_aI4_d];
		int[] t_rI4_gvi = new int[m_aI4_d];
		int[] t_rI4_gbestxi = m_aTC_gBest.getM_rI4_x();
		int[] t_rI4_xi = f_aTC_Pi.getM_rI4_x();
		int[] t_rI4_vi = f_aTC_Pi.getM_rI4_v();
		
		int t_aI4_e = m_aTC_random.nextInt(m_aI4_size);
		int t_aI4_f = m_aTC_random.nextInt(m_aI4_size);
		while(t_aI4_e==f_aI4_index){
			t_aI4_e = m_aTC_random.nextInt(m_aI4_size);
		}
		while(t_aI4_f == t_aI4_e || t_aI4_f == f_aI4_index){
			t_aI4_f = m_aTC_random.nextInt(m_aI4_size);
		}
		
		ParticleSolution t_aTC_Pe = (ParticleSolution) m_aTC_population[t_aI4_e];
		int[] t_rI4_xe = t_aTC_Pe.getM_rI4_x();
		ParticleSolution t_aTC_Pf = (ParticleSolution) m_aTC_population[t_aI4_f];
		int[] t_rI4_xf = t_aTC_Pf.getM_rI4_x();
		
		for(int t_aI4_j=0;t_aI4_j<m_aI4_d;t_aI4_j++){
			double t_aI8_temp_x = m_aI8_r4 * t_rI4_xi[t_aI4_j] + m_aI8_r5 * t_rI4_gbestxi[t_aI4_j] + m_aI8_r6 * (t_rI4_xe[t_aI4_j]-t_rI4_xf[t_aI4_j]);
			t_rI4_gxi[t_aI4_j] = new Double(Math.round(t_aI8_temp_x)).intValue();
			t_rI4_gvi[t_aI4_j] = t_rI4_vi[t_aI4_j];
			if(t_rI4_gxi[t_aI4_j] > m_aI4_xmax){
				t_rI4_gxi[t_aI4_j] = m_aI4_xmax;
			}else if(t_rI4_gxi[t_aI4_j] < m_aI4_xmin){
				t_rI4_gxi[t_aI4_j] = m_aI4_xmin;
			}
			if(t_rI4_gvi[t_aI4_j] > m_aI4_vmax){
				t_rI4_gvi[t_aI4_j] = m_aI4_vmax;
			}else if(t_rI4_gvi[t_aI4_j] < m_aI4_vmin){
				t_rI4_gvi[t_aI4_j] = m_aI4_vmin;
			}
		}
		
		t_aTC_Gi.setM_rI4_x(t_rI4_gxi);
		t_aTC_Gi.setM_rI4_v(t_rI4_gvi);
		return t_aTC_Gi;
	}
	
	/**
	 * ��a,b,c����������ѡ����ѵ�һ������
	 * @param f_aTC_a
	 * @param f_aTC_b
	 * @param f_aTC_c
	 * @return
	 */
	private ParticleSolution selectFittest(ParticleSolution f_aTC_a,ParticleSolution f_aTC_b,ParticleSolution f_aTC_c){
		ParticleSolution t_aTC_particle = null;
		if(Double.compare(f_aTC_a.getM_aI8_fitness(), f_aTC_b.getM_aI8_fitness())>0){
			if(Double.compare(f_aTC_a.getM_aI8_fitness(),f_aTC_c.getM_aI8_fitness())>0)
				t_aTC_particle = f_aTC_a;
			else
				t_aTC_particle = f_aTC_c;
		}else {
			if(Double.compare(f_aTC_b.getM_aI8_fitness(), f_aTC_c.getM_aI8_fitness())>0)
				t_aTC_particle = f_aTC_b;
			else
				t_aTC_particle = f_aTC_c;
		}
		
		return t_aTC_particle;
	}

	@Override
	protected ASolution solve(int f_aI4_p) {
		//��ʼ��
		{
			init();
			initPopulation(NameSpace.s_str_dnspso);
			initV();
			initBest();
		}
				
		//��ӡ���������ڲ��Ե���
		{
			/*printConstraint();
			System.out.println("==========��ʼ����Ⱥ==========");
			printPopulation(m_aTC_population);
			System.out.println("==========��ʷ������Ⱥ==========");
			printPopulation(m_aTC_pBest);
			System.out.println("==========ȫ����������==========");
			printSolution(m_aTC_gBest);*/
		}
				
		//��Ⱥ����
		{
			evolution();
		}
				
		//����ı���
		{
			String t_str_result_content = getResultContent(f_aI4_p, m_aTC_gBest);
			FileUtils.saveFile(m_str_data_path, m_str_alg_name, m_str_file_name, t_str_result_content);
		}
		
		return m_aTC_gBest;
	}
	
	public static void main(String[] args) {
		SSModel t_aTC_ssm = new SSModel(NameSpace.s_str_data_01);
		AStrategy t_aTC_strategy = new DNSPSOStrategy(10, 1000, 500, t_aTC_ssm, NameSpace.s_str_data_01);	
	    t_aTC_strategy.solve(0);
	}
	

}
