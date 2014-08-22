package org.arachnidium.model.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import net.sf.cglib.proxy.MethodProxy;

import org.apache.commons.lang3.ArrayUtils;
import org.arachnidium.core.fluenthandle.IHowToGetHandle;
import org.arachnidium.model.abstractions.ModelObjectInterceptor;
import org.arachnidium.model.support.HowToGetByFrames;
import org.arachnidium.model.support.annotations.classdeclaration.ClassDeclarationReader;
import org.arachnidium.model.support.annotations.classdeclaration.TimeOut;

public abstract class ApplicationInterceptor<IndexAnnotation extends Annotation, HandleUniqueIdentifiers extends Annotation, AdditionalStringIdentifier extends Annotation, HowTo extends IHowToGetHandle>
		extends ModelObjectInterceptor {
	
	private HowTo getHowToGetHandleStrategy(
			Class<IndexAnnotation> indexAnnotation,
			Class<HandleUniqueIdentifiers> handleUniqueIdentifiers,
			Class<AdditionalStringIdentifier> additionalStringIdentifier,
			Class<?> annotated, Class<HowTo> howToClass)
			throws ReflectiveOperationException {
		IndexAnnotation[] indexAnnotations = ClassDeclarationReader
				.getAnnotations(indexAnnotation, annotated);
		Integer index = null;
		if (indexAnnotations.length > 0) {
			index = ClassDeclarationReader.getIndex(indexAnnotations[0]);
		}

		HandleUniqueIdentifiers[] handleUniqueIdentifiers2 = ClassDeclarationReader
				.getAnnotations(handleUniqueIdentifiers, annotated);
		List<String> identifiers = ClassDeclarationReader
				.getRegExpressions(handleUniqueIdentifiers2);
		if (identifiers.size() == 0) {
			identifiers = null;
		}

		String additionalStringIdentifier2 = null;
		AdditionalStringIdentifier[] additionalStringIdentifiers = ClassDeclarationReader
				.getAnnotations(additionalStringIdentifier, annotated);
		if (additionalStringIdentifiers.length > 0) {
			additionalStringIdentifier2 = ClassDeclarationReader
					.getRegExpressions(additionalStringIdentifiers).get(0);
		}

		if (index == null && identifiers == null
				&& additionalStringIdentifier2 == null) {
			return null;
		}

		try {
			return howToClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw e;
		}
	}

	private Long getTimeOut(Class<?> annotated) {
		TimeOut[] timeOuts = ClassDeclarationReader.getAnnotations(
				TimeOut.class, annotated);
		if (timeOuts.length == 0) {
			return null;
		}
		return ClassDeclarationReader.getTimeOut(timeOuts[0]);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object intercept(Object application, Method method, Object[] args,
			MethodProxy methodProxy) throws Throwable {
		try {
			if (!method.getName().equals(GET_PART)) {
				return super.intercept(application, method, args, methodProxy);
			}

			List<Class<?>> paramClasses = Arrays.asList(method
					.getParameterTypes());
			Type generic = this.getClass().getGenericSuperclass();
			ParameterizedType pType = (ParameterizedType) generic;
			Class<IndexAnnotation> indexAnnotationClass = (Class<IndexAnnotation>) Class
					.forName(pType.getActualTypeArguments()[0].getTypeName());
			Class<HandleUniqueIdentifiers> huiA = (Class<HandleUniqueIdentifiers>) Class
					.forName(pType.getActualTypeArguments()[1].getTypeName());
			Class<AdditionalStringIdentifier> asiA = (Class<AdditionalStringIdentifier>) Class
					.forName(pType.getActualTypeArguments()[2].getTypeName());
			Class<HowTo> howTo = (Class<HowTo>) Class.forName(pType
					.getActualTypeArguments()[3].getTypeName());

			// There is nothing to do if all parameters apparently defined
			if (!paramClasses.contains(howTo)
					|| !paramClasses.contains(HowToGetByFrames.class)
					|| !paramClasses.contains(long.class)) {

				HowTo how = getHowToGetHandleStrategy(indexAnnotationClass,
						huiA, asiA, paramClasses
						// the first parameter is a class which instance we
						// want
								.get(0), howTo);

				int paramIndex = ModelSupportUtil.getParameterIndex(
						method.getParameters(), int.class);
				Integer index = null;
				if (paramIndex >= 0) {
					index = (Integer) args[paramIndex];
				}

				// if index of a window/screen was defined
				if (how != null && index != null) {
					how.setExpected(index.intValue());
				}

				HowToGetByFrames howToGetByFrames = null;
				if (!paramClasses.contains(HowToGetByFrames.class)) {
					howToGetByFrames = ifClassIsAnnotatedByFrames(paramClasses
					// the first parameter is a class which instance we want
							.get(0));
				}	
				
				Long timeOutLong = null;
				paramIndex = ModelSupportUtil.getParameterIndex(
						method.getParameters(), long.class);
				if (paramIndex >= 0) {
					timeOutLong = (Long) args[paramIndex];
				}
				else{
					timeOutLong = getTimeOut(paramClasses
					// the first parameter is a class which instance we want
							.get(0));
				}		
				
				//attempt to substitute methods is described below
				Object[] newArgs = new Object[] {args[0]};				
				if (how != null){
					newArgs = ArrayUtils.add(newArgs, how);
				}
				else if (index != null){
					int intIndex = index.intValue();
					newArgs = ArrayUtils.add(newArgs, intIndex);
				}
				
				if (howToGetByFrames != null){
					newArgs = ArrayUtils.add(newArgs, howToGetByFrames);
				}
				
				if (timeOutLong != null){
					long timeOut = timeOutLong.longValue();
					newArgs = ArrayUtils.add(newArgs, timeOut);
				}
				
				args = newArgs;
				method = ModelSupportUtil.getSuitableMethod(
						application.getClass(), GET_PART, args);
				methodProxy = ModelSupportUtil.getMethodProxy(
						application.getClass(), method);
				
			}
			return super.intercept(application, method, args, methodProxy);
		} catch (Exception e) {
			throw e;
		}
	}

}