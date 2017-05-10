package com.fzj.alg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.xml.transform.Templates;

import com.fzj.config.NameSpace;
import com.fzj.model.SSModel;
import com.fzj.solution.ASolution;
import com.fzj.utils.FileUtils;

/**
 * @author Fan Zhengjie
 * @date 2016��7��22�� ����3:48:31
 * @version 1.0
 * @description Ѳ�ߵ�λ����GA���
 */
public class GA extends AStrategy {

	private double m_aI8_pc;// �������
	private double m_aI8_pm;// �������
	public ASolution m_aTC_best;// ȫ�����Ž�
	private ASolution[] m_aTC_newPopulation;// ����Ⱥ���Ӹ���Ⱥ��ѡȡ�����н���ͱ���

	public GA() {
	}

	public GA(int f_aI4_size, int f_aI4_max_nfe, int f_aI4_max_iter, SSModel f_aTC_ssm, String f_str_data_path) {
		super(f_aI4_size, f_aI4_max_nfe, f_aI4_max_iter, f_aTC_ssm, f_str_data_path);
	}

	@Override
	protected void init() {
		this.m_aTC_population = new ASolution[m_aI4_size];// ����
		this.m_aTC_newPopulation = new ASolution[m_aI4_size];// �Ӵ�

		this.m_aI8_pc = 0.85;
		this.m_aI8_pm = 0.05;

		m_str_alg_name = NameSpace.s_str_ga;
		m_str_file_name = FileUtils.getResultName(m_str_alg_name, NameSpace.s_str_file_txt, m_aI4_max_nfe);

	}

	/**
	 * ��ֹ���뱨������Ĳ�����ʵ��GA����Ҫ���
	 */
	private void initV() {
		for (int t_aI4_k = 0; t_aI4_k < m_aI4_size; t_aI4_k++) {
			ASolution t_aTC_particle = m_aTC_population[t_aI4_k];
			int[] t_aI4_x = t_aTC_particle.getM_rI4_x();
			t_aTC_particle.setM_rI8_v(int2doubleArray(t_aI4_x));
			t_aTC_particle.setM_rI4_v2(t_aI4_x);
		}
	}

	/**
	 * ȫ�����ź���ʷ�������ӵĸ���
	 */
	private void initBest() {
		// ȫ�����Ž�ĳ�ʼ������
		m_aTC_best = new ASolution(m_aI4_d);
		m_aTC_best.setM_aI8_fitness(Double.MIN_VALUE);
		for(int t_aI4_i=0;t_aI4_i<m_aI4_size;t_aI4_i++){
			if(Double.compare(m_aTC_best.getM_aI8_fitness(), m_aTC_population[t_aI4_i].getM_aI8_fitness())<0){
				m_aTC_best = m_aTC_population[t_aI4_i].clone();
			}
		}
	}
	
	/**
	 * ������Ⱥ����������ۻ����ʣ�ǰ�����Ѿ�����������������Ӧ�ȣ�
	 * ��Ϊ���̶�ѡ����Ե�һ����
	 */
	private void countRate(){
		double t_aI8_sumFitness = 0.0;
		for(int t_aI4_i=0;t_aI4_i<m_aI4_size;t_aI4_i++){
			t_aI8_sumFitness += m_aTC_population[t_aI4_i].getM_aI8_fitness();
		}
		
		m_aTC_population[0].setM_aI8_pi(m_aTC_population[0].getM_aI8_fitness()/t_aI8_sumFitness);
		for(int t_aI4_i=1;t_aI4_i<m_aI4_size;t_aI4_i++){
			m_aTC_population[t_aI4_i].setM_aI8_pi(m_aTC_population[t_aI4_i].getM_aI8_fitness()/t_aI8_sumFitness + m_aTC_population[t_aI4_i-1].getM_aI8_pi());
		}
	}
	
	/**
	 * ��ѡĳ����Ⱥ����Ӧ����ߵĸ��壬ֱ�Ӹ��Ƶ��Ӵ�
	 */
	private void selectBest(){
		int t_aI4_maxid = 0;
		double t_aI8_maxFitness = m_aTC_population[0].getM_aI8_fitness();
		
		for(int t_aI4_i=1;t_aI4_i<m_aI4_size;t_aI4_i++){
			if(Double.compare(m_aTC_population[t_aI4_i].getM_aI8_fitness(), t_aI8_maxFitness) > 0){
				t_aI8_maxFitness = m_aTC_population[t_aI4_i].getM_aI8_fitness();
				t_aI4_maxid = t_aI4_i;
			}
		}
		
		if(Double.compare(m_aTC_best.getM_aI8_fitness(), t_aI8_maxFitness) < 0){
			m_aTC_best = m_aTC_population[t_aI4_maxid].clone();
		}
		
		//��������Ⱥ����Ӧ����ߵ�Ⱦɫ��maxid���Ƶ�����Ⱥ�У����ڵ�һλ
		copySolution(0, t_aI4_maxid);
		
	}
	
	/**
	 * ����Ⱦɫ��
	 * @param k1 ��ʾ��Ⱦɫ������Ⱥ�е�λ��
	 * @param k2 ��ʾ��Ⱦɫ������Ⱥ�е�λ��
	 */
	private void copySolution(int f_aI4_k1,int f_aI4_k2){
		m_aTC_newPopulation[f_aI4_k1] = m_aTC_population[f_aI4_k2].clone();
	}
	
	/**
	 * ���̶�ѡ�������ѡ
	 */
	private void select(){
		int t_aI4_selectId,t_aI4_j;
		
		for(int t_aI4_i=1;t_aI4_i<m_aI4_size;t_aI4_i++){//���ŵ��Ѿ�ֱ�Ӹ��Ƶ��Ӵ������Դ˴���ѡ��n-1������
			double t_aI8_r = (m_aTC_random.nextInt(65535) % 1000) / 1000.0;
			for(t_aI4_j=0;t_aI4_j<m_aI4_size;t_aI4_j++){
				if(Double.compare(t_aI8_r, m_aTC_population[t_aI4_j].getM_aI8_pi())<=0){
					break;
				}
			}
			t_aI4_selectId = t_aI4_j;
			copySolution(t_aI4_i, t_aI4_selectId);
		}
	}
	
	/**
	 * ��ζԻ���������
	 * @param k ѡ�񣬽��������Ⱥ�еĵ�k��Ⱦɫ��
	 */
	private void variation(int f_aI4_k){
		int t_aI4_x, t_aI4_y;//����λ��
	
		int count = m_aTC_random.nextInt(m_aI4_k)+1;//�Ի�����
		ASolution solution = m_aTC_newPopulation[f_aI4_k];
		int[] t_rI4_x = solution.getM_rI4_x();

		for(int i=0;i<count;i++){
			t_aI4_x = m_aTC_random.nextInt(65535) % m_aI4_d;
			t_aI4_y = m_aTC_random.nextInt(65535) % m_aI4_d;
			while(t_aI4_x==t_aI4_y || t_rI4_x[t_aI4_x]==t_rI4_x[t_aI4_y]){
				t_aI4_y = m_aTC_random.nextInt(65535) % m_aI4_d;
			}
			
			int t_rI4_temp = t_rI4_x[t_aI4_x];
			t_rI4_x[t_aI4_x] = t_rI4_x[t_aI4_y];
			t_rI4_x[t_aI4_y] = t_rI4_temp;
		}
		modify(m_aTC_newPopulation[f_aI4_k]);
	}
	
	/**
	 * ��������
	 * ��ͬȾɫ�������ͬ�Ӵ�Ⱦɫ��
	 * @param k1
	 * @param k2
	 */
	private void cross(int f_aI4_k1,int f_aI4_k2){
		int t_aI4_i, t_aI4_j, t_aI4_k, t_aI4_flag;
		int t_aI4_ran1, t_aI4_ran2, t_aI4_temp;
		int[] t_rI4_Gh1 = new int[m_aI4_d];
		int[] t_rI4_Gh2 = new int[m_aI4_d];
		int[] t_rI4_x1 = m_aTC_newPopulation[f_aI4_k1].getM_rI4_x();
		int[] t_rI4_x2 = m_aTC_newPopulation[f_aI4_k2].getM_rI4_x();

		t_aI4_ran1 = m_aTC_random.nextInt(65535) % m_aI4_d;
		t_aI4_ran2 = m_aTC_random.nextInt(65535) % m_aI4_d;
		while (t_aI4_ran1 == t_aI4_ran2) {
			t_aI4_ran2 = m_aTC_random.nextInt(65535) % m_aI4_d;
		}

		if (t_aI4_ran1 > t_aI4_ran2) {
			t_aI4_temp = t_aI4_ran1;
			t_aI4_ran1 = t_aI4_ran2;
			t_aI4_ran2 = t_aI4_temp;
		}
		
		for(t_aI4_i=0;t_aI4_i<t_aI4_ran1;t_aI4_i++){
			t_rI4_Gh1[t_aI4_i] = t_rI4_x2[t_aI4_i];
			t_rI4_Gh2[t_aI4_i] = t_rI4_x1[t_aI4_i];
		}
		for(t_aI4_i=t_aI4_ran1;t_aI4_i<t_aI4_ran2;t_aI4_i++){
			t_rI4_Gh1[t_aI4_i] = t_rI4_x1[t_aI4_i];
			t_rI4_Gh2[t_aI4_i] = t_rI4_x2[t_aI4_i];
		}
		for(t_aI4_i=t_aI4_ran2;t_aI4_i<m_aI4_d;t_aI4_i++){
			t_rI4_Gh1[t_aI4_i] = t_rI4_x2[t_aI4_i];
			t_rI4_Gh2[t_aI4_i] = t_rI4_x1[t_aI4_i];
		}

		for (t_aI4_i = 0; t_aI4_i < m_aI4_d; t_aI4_i++) {
			t_rI4_x1[t_aI4_i] = t_rI4_Gh1[t_aI4_i];// ������ϷŻ���Ⱥ
			t_rI4_x2[t_aI4_i] = t_rI4_Gh2[t_aI4_i];// ������ϷŻ���Ⱥ
		}
		
		modify(m_aTC_newPopulation[f_aI4_k1]);
		modify(m_aTC_newPopulation[f_aI4_k2]);
	}
	
	
	

	@Override
	protected void evolution() {
		while (m_aI4_cur_nfe < m_aI4_max_nfe) {
			evolution2();
			//������Ⱥ���Ƶ�����Ⱥ�У�׼����һ������
			for(int t_aI4_k=0;t_aI4_k<m_aI4_size;t_aI4_k++){
				m_aTC_population[t_aI4_k] = m_aTC_newPopulation[t_aI4_k];
			}
			//������Ⱥ��Ӧ��
			for(int t_aI4_k=0;t_aI4_k<m_aI4_size;t_aI4_k++){
				evaluate(m_aTC_population[t_aI4_k]);
				saveFitness(m_aTC_best);
				m_aI4_cur_nfe++;
			}
			
			//������Ⱥ�и���������ۻ�����
			countRate();
			
			
			m_aI4_cur_iter++;
		}
		System.out.println(m_str_alg_name + " m_aI4_cur_nfe = " + m_aI4_cur_nfe);
		System.out.println(m_str_alg_name + " m_aI4_cur_iter = " + m_aI4_cur_iter);

	}
	
	protected void evolution2() {
		int t_aI4_k;
		selectBest();
		select();
		
		double t_aI8_r;
		int t_aI4_count = m_aI4_size;
		if(t_aI4_count%2!=0){
			t_aI4_count++;
		}
		for(t_aI4_k=1;t_aI4_k<t_aI4_count-1;t_aI4_k+=2){
			t_aI8_r = m_aTC_random.nextDouble();
			if(Double.compare(t_aI8_r, m_aI8_pc)<0){
				cross(t_aI4_k, t_aI4_k+1);
			}
			else {
				t_aI8_r = m_aTC_random.nextDouble();
				if(Double.compare(t_aI8_r, m_aI8_pm)<0){
					variation(t_aI4_k);
				}
				
				t_aI8_r = m_aTC_random.nextDouble();
				if(Double.compare(t_aI8_r, m_aI8_pm)<0){
					variation(t_aI4_k+1);
				}
			}
		}
		if(m_aI4_size%2==0){//ʣ���һ��Ⱦɫ��û�н���
			t_aI8_r = m_aTC_random.nextDouble();
			if(Double.compare(t_aI8_r, m_aI8_pm)<0){
				variation(m_aI4_size-1);
			}
		}
	}

	@Override
	protected ASolution solve(int f_aI4_p) {
		// ��ʼ��
		{
			init();
			initPopulation(NameSpace.s_str_ga);
			countRate();//�����ʼ����Ⱥ�и���������ۻ�����
			initV();
			initBest();
			
		}

		// ��ӡ���������ڲ��Ե���
		{

		/*	System.out.println("==========��ʼ����Ⱥ==========");
			printPopulation(m_aTC_population);
			System.out.println("==========��ǰ���Ž�==========");
			printSolution(m_aTC_best);*/
		}

		// ��Ⱥ����
		{
			evolution();
		}

		// ����ı���
		{
			String t_str_result_content = getResultContent(f_aI4_p, m_aTC_best);
			FileUtils.saveFile(m_str_data_path, m_str_alg_name, m_str_file_name, t_str_result_content);
		}

		return m_aTC_best;
	}

	public static void main(String[] args) {
		SSModel t_aTC_ssm = new SSModel(NameSpace.s_str_data_01);
		AStrategy t_aTC_strategy = new GA(10, 1000, 500, t_aTC_ssm, NameSpace.s_str_data_01);
		t_aTC_strategy.solve(0);
	}
}
