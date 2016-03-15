package com.carloscardona.ruleengine.business;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.WorkingMemory;
import org.drools.compiler.PackageBuilder;
import org.drools.rule.Package;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.carloscardona.ruleengine.exception.RuleEngineException;

@Service
@Scope("singleton")
public class RuleEngineServicesImpl implements RuleEngineServices {

	private static final Logger LOGGER = LoggerFactory.getLogger(RuleEngineServicesImpl.class);

	private static final String REGLA = "/drools/drool.drl";

	/**
	 * Parseamos y compilamos las reglas en un unico paso. Verificamos el builder para ver si hubo errores. Obtenemos el package de reglas
	 * compilado. Agregamos el paquete a la base de reglas (desplegamos el paquete de reglas).
	 * 
	 * @param parametros
	 */
	@Override
	public void ejecutar(List<Object> parametros) {
		try {
			PackageBuilder builder = new PackageBuilder();
			Reader source = new InputStreamReader(RuleEngineServicesImpl.class.getResourceAsStream(REGLA));
			builder.addPackageFromDrl(source);

			if (builder.hasErrors()) {
				LOGGER.info(builder.getErrors().toString());
				throw new RuleEngineException("No se pudo compilar el archivo de reglas.");
			}

			Package pkg = builder.getPackage();

			RuleBase ruleBase = RuleBaseFactory.newRuleBase();
			ruleBase.addPackage(pkg);

			WorkingMemory workingMemory = ruleBase.newStatefulSession();

			List<Object> listResult = parametros;
			for (Object obj : parametros) {
				workingMemory.insert(obj);
			}
			workingMemory.fireAllRules();

			for (Object result : listResult) {
				LOGGER.info("result" + result);
			}
		} catch (Exception e) {
			LOGGER.error("Error al ejecutar la regla ", e);
			throw new RuleEngineException("No se pudo compilar el archivo de reglas.");
		}
	}
}
